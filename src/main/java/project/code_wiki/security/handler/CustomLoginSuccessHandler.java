package project.code_wiki.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
// 로그인 성공 핸들러
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            // 리다이렉트할 경로 지정
            String redirectUrl = (String)session.getAttribute("prevPage");
            if(redirectUrl != null) {
                // 세션에서 이전페이지 정보 삭제
                session.removeAttribute("prevPage");
                // 리다이렉트
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                // SuperClass 에서 처리
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } else {
            // SuperClass 에서 처리
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
