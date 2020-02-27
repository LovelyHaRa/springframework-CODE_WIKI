package project.code_wiki.dto;

import lombok.*;
import lombok.experimental.Delegate;

import java.time.LocalDateTime;

// 문서 히스토리 리스트 데이터 전달 객체 (SubClass of WikiList)

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HistoryListDto extends WikiListDto {
    // 필드
    @Delegate
    private Long revisionDoc;

    @Builder
    public HistoryListDto(String codeId, String codeName, String userName, LocalDateTime updateDate, Long revisionDoc) {
        super(codeId, codeName, userName, updateDate);
        this.revisionDoc = revisionDoc;
    }
}
