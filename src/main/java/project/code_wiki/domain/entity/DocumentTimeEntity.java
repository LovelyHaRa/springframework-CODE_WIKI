package project.code_wiki.domain.entity;

// 데이터 조작 시 자동으로 날짜를 수정해주는 'JPA' 의 Auditing 기능을 활용

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter // getter 메서드를 자동으로 생성
@MappedSuperclass // 테이블로 매핑하지 않고, 자식 클래스(엔티티)에게 매핑정보를 상속하기 위한 어노테이션)
@EntityListeners(AuditingEntityListener.class) // JPA 에게 해당 Entity 는 Auditing 기능을 사용한다는 것을 알림
public abstract class DocumentTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime updateDate;
}
