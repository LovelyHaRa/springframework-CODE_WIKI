package project.code_wiki.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
// JPA 에서 named native query 결과를 담당할 Entity
// 위키 문서의 코드별 최신 문서를 저장할 수 있는 데이터 Entity
@Getter
@Setter
public class DocumentDistinctCodeIdEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    // 필드 정의(DocumentEntity 필드와 동일)
    private Long id;
    private String barcode_id;
    private Long revision_doc;
    private String user_id;
    private String data;
    private LocalDateTime update_date;

    // 생성자를 통해 쿼리에서 인스턴스 저장이 가능하다
    public DocumentDistinctCodeIdEntity(Long id, String barcode_id, Long revision_doc, String user_id, String data, LocalDateTime update_date) {
        this.id = id;
        this.barcode_id = barcode_id;
        this.revision_doc = revision_doc;
        this.user_id = user_id;
        this.data = data;
        this.update_date = update_date;
    }
}
