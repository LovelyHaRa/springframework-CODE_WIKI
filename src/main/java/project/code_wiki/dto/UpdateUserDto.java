package project.code_wiki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

// 유저 정보 데이터 전달 객체

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserDto {
    // 필드
    @Email
    private String email;
    @NotBlank(message = "필수 사항입니다.")
    private String name;
    @NotBlank(message = "필수 사항입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대, 소문자와 숫자, 특수기호가 1개이상 포함, 8~20자")
    private String newPassword;
    @NotBlank(message = "필수 사항입니다.")
    private String confirmPassword;
}
