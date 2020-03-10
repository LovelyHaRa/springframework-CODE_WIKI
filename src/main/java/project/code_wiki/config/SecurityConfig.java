package project.code_wiki.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import project.code_wiki.security.handler.CustomLoginFailureHandler;
import project.code_wiki.security.handler.CustomLoginSuccessHandler;
import project.code_wiki.service.UserService;
// Spring Security 설정 클래스
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;
    // 패스워드 인코딩 클래시를 Bean 으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 로그인 인증 처리 방식 설정(모든 인증은 AuthenticationManager 를 통해 이루어짐)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder((passwordEncoder())); // 인증은 UserService 에서 이루어짐
    }
    // FilterChainProxy 를 생성하는 필터
    @Override
    public void configure(WebSecurity web) {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    // HTTP 요청에 대한 웹 기반 보안을 구성
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 페이지 권한 설정
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/mypage").hasRole("USER")
                .antMatchers("/board/write").hasRole("USER")
                .antMatchers("/board/post/edit/**").hasRole("USER")
                .antMatchers("/wiki/edit/**").hasRole("USER")
                .antMatchers("/**").permitAll()
                .and()
                // 로그인 설정
                .formLogin()
                .loginPage("/user/login")
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .permitAll()
                .and()
                // 로그아웃 설정
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/logout/result")
                .invalidateHttpSession(true)
                .and()
                // 403 예외처리 핸들링
                .exceptionHandling().accessDeniedPage("/denied");
        http.csrf().ignoringAntMatchers("/admin/**", "/api/**");
    }

    // 로그인 성공했을 때 처리 핸들러 등록 (이전 페이지로 리다이렉트)
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/");
    }
    // 로그인 실패 시 처리 핸들러 등록
    @Bean
    public AuthenticationFailureHandler failureHandler() { return new CustomLoginFailureHandler(); }

}
