package project.code_wiki.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

// 문서 데이터 리스트 전달 객체 (getter, setter 로만 구성되있음)

@Getter
@Setter
@ToString
@NoArgsConstructor
public class WikiListDto {
    // 필드
    private String codeId;
    private String codeName;
    private String userName;
    private LocalDateTime updateDate;

    public WikiListDto(String codeId, String codeName, String userName, LocalDateTime updateDate) {
        this.codeId = codeId;
        this.codeName = codeName;
        this.userName = userName;
        this.updateDate = updateDate;
    }
}
