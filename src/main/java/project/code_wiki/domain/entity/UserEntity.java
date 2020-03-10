package project.code_wiki.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import project.code_wiki.dto.DataStatisticDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

// 유저 데이터 표현
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "user")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "UserEntity.getUserWeekCount",
                query = "SELECT substring(register_date_time,1,10) as date, COUNT(*) AS count " +
                        "FROM user " +
                        "WHERE DATE(register_date_time) >= DATE_SUB(NOW(), INTERVAL 7 DAY) " +
                        "GROUP BY date;",
                resultSetMapping = "UserEntity.getUserWeekCount"
        )
})
@SqlResultSetMapping(
        name = "UserEntity.getUserWeekCount",
        classes = {
                @ConstructorResult(
                        targetClass = DataStatisticDto.class,
                        columns = {
                                @ColumnResult(name = "date", type = LocalDate.class),
                                @ColumnResult(name = "count", type = Long.class),
                        }
                )
        }
)
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
