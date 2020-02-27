package project.code_wiki.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

// 유저 데이터 표현
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    private String email; // 이메일 주소
    @JsonIgnore // 해당 필드는 JSON 데이터 수집을 하지 않음
    @Column(length = 100, nullable = false)
    private String password; // 비밀번호
    @Column(length = 100, nullable = false)
    private String name; // 유저 이름
    @Column
    private LocalDateTime registerDateTime; // 생성 일자
}
