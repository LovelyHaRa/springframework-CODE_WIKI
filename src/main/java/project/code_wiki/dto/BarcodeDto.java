package project.code_wiki.dto;

import lombok.*;
import project.code_wiki.domain.entity.BarcodeEntity;

// 바코드 데이터 전달 객체 (getter, setter 로만 구성되있음)

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BarcodeDto {
    // 필드
    private String id;
    private String name;
    private Long latelyRevision;

    // DTO 에서 필요한 부분을 빌더 패턴을 통해 entity 로 만듬
    public BarcodeEntity toEntity() {
        return BarcodeEntity.builder()
                .id(id).name(name).latelyRevision(latelyRevision)
                .build();
    }
}
