package project.code_wiki.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.code_wiki.domain.entity.DocumentEntity;
import project.code_wiki.domain.entity.DocumentDistinctCodeIdEntity;

import java.util.Optional;

// JPA 데이터 조작을 담당하는 인터페이스
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    Optional<DocumentEntity> findByBarcodeIdAndRevisionDoc(String id, Long revisionDoc); // 해당 코드의 최신 문서 찾기
    Page<DocumentEntity> findByBarcodeId(String id, Pageable pageable); // 바코드 찾기(페이징)
    Page<DocumentEntity> findDocList(Pageable pageable); // 문서 리스트(페이징)
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(nativeQuery = true)
    Page<DocumentDistinctCodeIdEntity> findDocListOrderByLength(Pageable pageable); // 내용이 긴 문서 리스트(페이징)
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(nativeQuery = true)
    Page<DocumentDistinctCodeIdEntity> findDocListOrderByLengthAsc(Pageable pageable); // 내용이 짧은 문서 리스트(페이징)
    Long countByBarcodeId(String id); // 등록된 코드 정보 카운팅
}
