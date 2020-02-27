package project.code_wiki.dto;

import lombok.*;

import java.time.LocalDateTime;

// 위키 문서 데이터 전달 객체

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class WikiDto {
    // 필드
    private String codeId;
    private String codeName;
    private LocalDateTime updateDate;
    private String data;
}
