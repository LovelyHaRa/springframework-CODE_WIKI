package project.code_wiki.dto;

import lombok.*;
import project.code_wiki.domain.entity.BoardEntity;

import java.time.LocalDateTime;

// 게시판 데이터 전달 객체 (getter, setter 로만 구성되있음)

@Getter // getter 자동 생성
@Setter // setter 자동 생성
@ToString // ToString 매서드 자동 생성
@Builder // 빌더로 생성자 생성
@AllArgsConstructor // 모든 종류의 생성자 추가
@NoArgsConstructor // 파라미터가 없는 기본 생성자 추가
public class BoardDto {
    // 필드
    private Long id;
    private String email;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long hit;

    // DTO 에서 필요한 부분을 빌더 패턴을 통해 entity 로 만듬
    public BoardEntity toEntity() {
        return BoardEntity.builder()
                .id(id).email(email).writer(writer).title(title).content(content).hit(hit)
                .build();
    }
}
