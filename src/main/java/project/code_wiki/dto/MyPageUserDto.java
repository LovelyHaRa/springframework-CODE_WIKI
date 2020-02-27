package project.code_wiki.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

// 유저 정보 데이터 전달 객체 (SubClass of UpdateUserDto)

@Getter
@Setter
public class MyPageUserDto extends UpdateUserDto {
    // 필드
    @NotBlank(message = "필수 사항입니다.")
    private String password;

    public MyPageUserDto(String email, String name, String newPassword, String confirmPassword, String password) {
        super(email, name, newPassword, confirmPassword);
        this.password = password;
    }
}
