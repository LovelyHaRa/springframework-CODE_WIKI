package project.code_wiki.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.code_wiki.domain.entity.BoardEntity;
import project.code_wiki.dto.DataStatisticDto;

import java.util.List;

// JPA 데이터 조작을 담당하는 인터페이스
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByTitleContaining(String keyword); // 제목으로 검색
    Page<BoardEntity> findAll(Pageable pageable);
    @Query(nativeQuery = true)
    List<DataStatisticDto> getPostWeekCount();
}
