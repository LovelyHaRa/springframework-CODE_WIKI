package project.code_wiki.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.code_wiki.common.ResultMessage;
import project.code_wiki.dto.MyPageUserDto;
import project.code_wiki.dto.UpdateUserDto;
import project.code_wiki.dto.UserDto;
import project.code_wiki.exception.AccessDeniedException;
import project.code_wiki.security.CustomUserDetails;
import project.code_wiki.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
// 사용자 컨트롤러
@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    // 회원가입 페이지
    @GetMapping("/user/signup")
    public String join(UserDto userDto, Model model) {
        // 타임리프 변수 참조 에러를 방지하기 위해 정의해준다.
        if (userDto==null) {
            model.addAttribute("userDto", null);
        }
        return "user/sign-up.html";
    }

    // 회원가입 처리
    @PostMapping("/user/signup")
    public String join(@Valid UserDto userDto, Errors errors, Model model) {
        // Back-End 검증
        if (errors.hasErrors()) {
            // 회원 가입 실패 시 입력 데이터를 유지
            model.addAttribute("userDto", userDto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                // 검증 실패 메시지 전달
                model.addAttribute(key, validatorResult.get(key));
                String attr = key+"Attr";
                // 검증 실패를 css 에서 확인할 수 있는 클래스 전달
                model.addAttribute(attr, "is-invalid");
            }
            // 같은 페이지 로드, (GET 요청에서 userDto 를 전달해 주지 않으면 페이지가 로드되지 않는다.)
            return "user/sign-up.html";
        }
        // 검증 통과 시 유저 정보 저장
        userService.joinUser(userDto);
        // 성공 페이지(임시로 메인페이지로 이동)
        return "redirect:/wiki";
    }

    // 로그인 페이지 (GET, POST 모두 처리)
    @RequestMapping("/user/login")
    public String userLogin(HttpServletRequest request) {
        // 세션에 이전페이지 정보가 없으면 이전페이지 정보 추가
        if (request.getSession().getAttribute("prevPage")==null) {
            // 전 페이지의 URI 정보를 반환
            String referer = request.getHeader("Referer");
            // 세션에 이전페이지 정보 저장
            request.getSession().setAttribute("prevPage", referer);
        }
        return "user/login.html";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/user/logout/result")
    public String userLogoutResult() {
        return "redirect:/";
    }

    // 접근 거부 처리
    @GetMapping("/denied")
    public String denied(AccessDeniedException e, Model model) {
        String message = e.getMessage();
        // 거부 이유 전달
        model.addAttribute("message", message);
        return "error/403";
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/user/find")
    public String findView(UserDto userDto, Model model) {
        // 타임리프 변수 참조 에러를 방지하기 위해 정의해준다.
        if (userDto==null) {
            model.addAttribute("userDto", null);
        }
        return "user/find-password.html";
    }

    // 비밀번호 찾기 처리
    @PostMapping("/user/find")
    public String findUser(UserDto userDto, Model model) {
        // 1. 이메일 일치여부 확인
        if (!userService.isExistUser(userDto.getEmail())) {
            // 검증 실패 메시지 전달
            model.addAttribute("emailMessage", ResultMessage.NOT_EXIST_EMAIL.getValue());
            // 검증 실패를 css 에서 확인할 수 있는 클래스 전달
            model.addAttribute("valid_email", "is-invalid");
            return "user/find-password";
        }
        // 2. 이메일이 일치하면 사용자 이름이 일치하는지 확인
        if (!userService.isExistUser(userDto.getEmail(), userDto.getName())) {
            // 입력정보 유지
            model.addAttribute("userDto", userDto);
            // 검증 실패 메시지 전달
            model.addAttribute("nameMessage", ResultMessage.NOT_MATCH_USERNAME.getValue());
            // 검증 실패를 css 에서 확인할 수 있는 클래스 전달
            model.addAttribute("valid_name", "is-invalid");
            return "user/find-password";
        }
        // 3. 사용자 정보 확보 성공, 비밀번호 초기화 페이지로 데이터 전달.
        model.addAttribute("isExistEmail", userDto.getEmail());
        model.addAttribute("isExistName", userDto.getName());
        return "user/change-password";
    }

    // 비밀번호 초기화
    @PostMapping("/user/find/changePassword")
    public String changePassword(@Valid UpdateUserDto updateUserDto, Errors errors, Model model) {
        // 1. 패스워드 검증
        if (errors.hasErrors()) {
            // 입력정보 유지
            model.addAttribute("isExistEmail", updateUserDto.getEmail());
            model.addAttribute("isExistName", updateUserDto.getName());
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                // 검증 실패 메시지 전달
                model.addAttribute(key, validatorResult.get(key));
                String attr = key+"Attr";
                // 검증 실패를 css 에서 확인할 수 있는 클래스 전달
                model.addAttribute(attr, "is-invalid");
            }
            return "user/change-password";
        }
        // 2. 새로운 패스워드 일치 여부 검사
        if (!updateUserDto.getNewPassword().equals(updateUserDto.getConfirmPassword())) {
            // 입력정보 유지
            model.addAttribute("isExistEmail", updateUserDto.getEmail());
            model.addAttribute("isExistName", updateUserDto.getName());
            // 검증 실패 메시지 전달
            model.addAttribute("passwordMessage", ResultMessage.NOT_MATCH_PASSWORD.getValue());
            // 검증 실패를 css 에서 확인할 수 있는 클래스 전달
            model.addAttribute("valid_newPasswordAttr", "is-invalid");
            model.addAttribute("valid_confirmPasswordAttr", "is-invalid");
            return "user/change-password";
        }
        // 3. 검증 성공, 비밀번호 변경
        userService.updateUser(updateUserDto);
        return "user/change-success.html";
    }

    // 유저 정보 패이지
    @GetMapping("/user/mypage")
    public String userMyPage(@AuthenticationPrincipal CustomUserDetails userDetails, String resultMsg, Model model) {
        // 타임리프 변수 참조 에러를 방지하기 위해 정의해준다.
        if(resultMsg==null) {
            model.addAttribute("resultMsg", null);
        }
        // 사용자 데이터 전달
        model.addAttribute("email", userDetails.getUsername());
        model.addAttribute("name", userDetails.getName());
        return "user/mypage.html";
    }

    // 유저 정보 수정
    @PostMapping("/user/mypage")
    public String updateUserInfo(@Valid MyPageUserDto updateUserDto, Errors errors, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // Back-End 검증
        // 리다이렉트시 정보 유지
        this.keepUserData(model, userDetails.getUsername(), userDetails.getName());
        // 1. 기존 비밀번호 일치여부 확인
        if(!userService.isPasswordEqual(updateUserDto)) {
            // 검증 실패 메시지 전달
            model.addAttribute("resultMsg", ResultMessage.NOT_MATCH_PASSWORD.getValue());
            return "user/mypage";
        }
        // 2. 새로운 패스워드 일치 여부 검사
        if (!updateUserDto.getNewPassword().equals(updateUserDto.getConfirmPassword())) {
            // 검증 실패 메시지 전달
            model.addAttribute("resultMsg", ResultMessage.NOT_EQUAL_NEW_PASSWORD.getValue());
            return "user/mypage";
        }
        // 3. 입력정보 검증
        if(errors.hasErrors()) {
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "user/mypage";
        }
        // 4. 회원정보 수정
        userService.updateUser(updateUserDto);
        model.addAttribute("resultMsg", ResultMessage.SUCCESS_UPDATE_USER.getValue());
        return "user/mypage";
    }

    // 정보 유지 공통처리
    private void keepUserData(Model model, String email, String name) {
        model.addAttribute("email", email);
        model.addAttribute("name", name);
    }

    // 관리자 페이지 리다이렉트
    @GetMapping("/admin")
    public String admin() {
        return "admin/index.html";
    }

    // 데이터테이블 뷰
//    @GetMapping("/admin/{type}")
//    public String viewTable(@PathVariable("type") String type, Model model) {
//        model.addAttribute("type", type);
//
//        return "admin/index.html";
//    }

    // 관리자 페이지에서 User 테이블 조회
    @GetMapping("/admin/user")
    public String viewTable(Model model) {
        model.addAttribute("type", "user");
        model.addAttribute("userList", userService.getUserData());
        return "admin/index.html";
    }

    // Restful API 사용, User 삭제 처리
    @DeleteMapping("/admin/user/delete")
    public String deleteUserByAdmin(String email) {
        // 1. 게시글 삭제
        userService.deleteUser(email);
        // 2. 게시판 목록으로 이동
        return "redirect:/admin/user";
    }
}
