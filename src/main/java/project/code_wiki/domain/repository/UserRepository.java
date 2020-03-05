package project.code_wiki.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.code_wiki.domain.entity.UserEntity;
import project.code_wiki.dto.DataTableDto;

import java.util.List;
import java.util.Optional;

// JPA 데이터 조작을 담당하는 인터페이스
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email); // 이메일 찾기
    Optional<UserEntity> findByEmailAndName(String email, String name); // 이메일과 이름을 동시에 만족하는 데이터 찾기
    Page<UserEntity> findAll(Pageable pageable);
    void deleteByEmail(String email);
}
