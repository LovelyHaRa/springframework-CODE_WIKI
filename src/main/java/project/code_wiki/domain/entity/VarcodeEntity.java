package project.code_wiki.domain.entity;

import lombok.*;

import javax.persistence.*;

// 바코드 데이터 표현
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
@AllArgsConstructor
@Entity
@NamedNativeQuery(
        // 랜덤으로 바코드 정보를 불러오는 쿼리
        name = "VarcodeEntity.findIdRandom",
        query = "SELECT * from varcode order by rand() limit 1",
        resultClass = VarcodeEntity.class
)
@Table(name = "varcode")
public class VarcodeEntity {
    // 필드 정의
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private Long latelyRevision;
}
