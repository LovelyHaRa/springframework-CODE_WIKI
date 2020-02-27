package project.code_wiki.domain.entity;

import lombok.*;

import javax.persistence.*;

// 게시판 데이터 표현
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 만들어주는 애노테이션
@Getter // 모든 필드에 getter 메서드 자동 생성
@Entity // JPA 에게 객체를 테이블과 매핑할 엔티티라고 알려주는 역할
@Table(name = "board") // 테이블 정보를 명시
@Builder
@AllArgsConstructor
public class BoardEntity extends BoardTimeEntity {
    // 필드 정의
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column
    private Long hit;
}
