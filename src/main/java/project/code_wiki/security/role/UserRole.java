package project.code_wiki.security.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
// 사용자 ROLE 정보 저장
@AllArgsConstructor
@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String value;
}
