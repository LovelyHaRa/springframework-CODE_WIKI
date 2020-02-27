package project.code_wiki.dto;

import lombok.*;
import project.code_wiki.domain.entity.DocumentEntity;

import java.time.LocalDateTime;

// 문서 정보 데이터 전달 객체 (getter, setter 로만 구성되있음)

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    // 필드
    private Long id;
    private String varcodeId;
    private Long revisionDoc;
    private String userId;
    private String data;
    private LocalDateTime updateDate;

    // DTO 에서 필요한 부분을 빌더 패턴을 통해 entity 로 만듬
    public DocumentEntity toEntity() {
        return DocumentEntity.builder()
                .id(id).varcodeId(varcodeId).revisionDoc(revisionDoc).userId(userId).data(data)
                .build();
    }
}
