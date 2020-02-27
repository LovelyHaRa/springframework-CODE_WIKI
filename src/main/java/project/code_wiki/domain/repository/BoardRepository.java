package project.code_wiki.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.code_wiki.domain.entity.BoardEntity;

import java.util.List;

// JPA 데이터 조작을 담당하는 인터페이스
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByTitleContaining(String keyword); // 제목으로 검색
}
