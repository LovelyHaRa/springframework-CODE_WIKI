package project.code_wiki.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

// 문서 히스토리 리스트 데이터 전달 객체 (SubClass of WikiList)

@ToString
@NoArgsConstructor
public class OrderedListDto extends WikiListDto {

    @Builder
    public OrderedListDto(String codeId, String codeName, String userName, LocalDateTime updateDate) {
        super(codeId, codeName, userName, updateDate);
    }
}
