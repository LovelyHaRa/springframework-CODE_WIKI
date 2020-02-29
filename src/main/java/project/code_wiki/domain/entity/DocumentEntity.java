package project.code_wiki.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

// 위키 문서 데이터 표현
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
@AllArgsConstructor
@Entity
@NamedQuery(
        // 코드 중복을 제외한 문서 데이터 추출
        name = "DocumentEntity.findDocList",
        query = "SELECT d FROM DocumentEntity d GROUP BY d.barcodeId"
)
@NamedNativeQueries({
        @NamedNativeQuery(
                // 내용이 긴 문서 쿼리
                name = "DocumentEntity.findDocListOrderByLength",
                query = "SELECT document.id, barcode_id, revision_doc, user_id, `data`,update_date " +
                        "FROM document INNER JOIN barcode ON document.barcode_id = barcode.id " +
                        "WHERE lately_revision = revision_doc " +
                        "ORDER BY CHAR_LENGTH(DATA) Desc",
                resultSetMapping = "DocumentEntity.findDocListOrderByLength"
        ),
        @NamedNativeQuery(
                // 내용이 짧은 문서 쿼리
                name = "DocumentEntity.findDocListOrderByLengthAsc",
                query = "SELECT document.id, barcode_id, revision_doc, user_id, `data`,update_date " +
                        "FROM document INNER JOIN barcode ON document.barcode_id = barcode.id " +
                        "WHERE lately_revision = revision_doc " +
                        "ORDER BY CHAR_LENGTH(DATA)",
                resultSetMapping = "DocumentEntity.findDocListOrderByLength"
        )
})
@SqlResultSetMapping(
        // 네이티브 쿼리 결과를 Mapping 하기 위한 설정 정의
        name = "DocumentEntity.findDocListOrderByLength",
        classes = {
                @ConstructorResult(
                        targetClass = DocumentDistinctCodeIdEntity.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "barcode_id", type = String.class),
                                @ColumnResult(name = "revision_doc", type = Long.class),
                                @ColumnResult(name = "user_id", type = String.class),
                                @ColumnResult(name = "data", type = String.class),
                                @ColumnResult(name = "update_date", type = LocalDateTime.class)
                        }
                )
        }
)
@Table(name = "document")
public class DocumentEntity extends DocumentTimeEntity {
    // 필드 정의
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String barcodeId;
    @Column
    private Long revisionDoc;
    @Column
    private String userId;
    @Column
    private String data;
}
