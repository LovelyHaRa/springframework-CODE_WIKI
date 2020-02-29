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
        name = "BarcodeEntity.findIdRandom",
        query = "SELECT * from barcode order by rand() limit 1",
        resultClass = BarcodeEntity.class
)
@Table(name = "barcode")
public class BarcodeEntity {
    // 필드 정의
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private Long latelyRevision;
}
