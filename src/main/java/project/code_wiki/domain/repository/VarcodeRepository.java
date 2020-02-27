package project.code_wiki.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.code_wiki.domain.entity.VarcodeEntity;

import java.util.Optional;

// JPA 데이터 조작을 담당하는 인터페이스
public interface VarcodeRepository extends JpaRepository<VarcodeEntity, String> {
    Page<VarcodeEntity> findByIdContainingOrNameContaining(String id, String name, Pageable pageable); // id와 이름 모두 일치하는 데이터 검색(페이징)
    Optional<VarcodeEntity> findByIdContainingOrNameContaining(String id, String name); // id와 이름 모두 일치하는 데이터 검색
    Optional<VarcodeEntity> findByIdLikeOrNameLike(String id, String name); // id와 이름 일부가 포함되는 데이터 검색
    Optional<VarcodeEntity> findIdRandom(); // 무작위로 바코드 정보 가져오기
}
