package project.code_wiki.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import project.code_wiki.common.ResultMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// 로그인 실패 핸들러
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMsg;
        if (exception instanceof BadCredentialsException) {
            errorMsg = ResultMessage.NOT_EQUAL_EMAIL_OR_PASSWORD.getValue();
            // 로그인 실패 이유 메시지 데이터 전달
            request.setAttribute("failureMessage", errorMsg);
            // 이전 페이지 정보를 유지하기 위해 세션 내 이전 페이지 정보를 갱신
            request.getSession().setAttribute("prevPage", request.getSession().getAttribute("prevPage"));
            // 모든 요청 정보를 유지한 채 디스패치
            request.getRequestDispatcher("/user/login").forward(request, response);
        }
    }
}
