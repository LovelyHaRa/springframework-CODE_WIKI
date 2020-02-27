package project.code_wiki.dto;

import lombok.*;
import project.code_wiki.common.annotation.UniqueEmail;
import project.code_wiki.domain.entity.UserEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

// 유저 데이터 전달 객체 (getter, setter 로만 구성되있음)

@Getter
@Setter
@ToString // 자동으로 필드들을 ToString 으로 출력해주는 메서드 생성
@NoArgsConstructor
public class UserDto {
    // 필드
    @NotBlank(message = "필수 사항입니다.")
    @Email
    @UniqueEmail
    private String email;
    @NotBlank(message = "필수 사항입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "8~20자, 영문 대/소문자, 숫자, 특수기호가 각 1개이상 포함")
    private String password;
    @NotBlank(message = "필수 사항입니다.")
    @Size(min = 2, max = 8, message = "2~8자 범위의 이름만 가능합니다")
    private String name;

    // DTO 에서 필요한 부분을 빌더 패턴을 통해 entity 로 만듬
    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(email).password(password).name(name).registerDateTime(LocalDateTime.now())
                .build();
    }
    // 빌더를 이렇게도 사용할 수 있다.
    @Builder
    public UserDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
