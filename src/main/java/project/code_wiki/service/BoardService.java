package project.code_wiki.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.code_wiki.common.PageController;
import project.code_wiki.common.ResultMessage;
import project.code_wiki.domain.entity.BoardEntity;
import project.code_wiki.domain.repository.BoardRepository;
import project.code_wiki.dto.BoardDto;
import project.code_wiki.exception.NotFoundPostException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 게시판 기능들의 요청을 처리하는 객체
@Service
@AllArgsConstructor
public class BoardService {

    private BoardRepository boardRepository; // JPA 데이터 조작을 담당하는 객체

    // 게시글 데이터 모두 불러오기
    @Transactional
    public List<BoardDto> getPostList(Integer pageNum) {
        // 1. 게시글 리스트 불러오기(한 페이지에 담을 수 있는 개수만큼만 불러오기)
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(pageNum - 1, PageController.PAGE_POST_COUNT, Sort.by((Sort.Direction.DESC), "createdDate")));
        // 2. Page -> List 변환
        List<BoardEntity> boardEntities = page.getContent();
        // 3. Entity -> DTO 변환
        List<BoardDto> boardDtoList = new ArrayList<>(); // Entity 를 Dto 에 주입시키기 위해 선언
        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }
        // 4. 리턴
        return boardDtoList;
    }

    // 게시글 카운팅
    @Transactional
    public Long getPostCount() {
        return boardRepository.count();
    }

    // 지정된 포스트 하나 불러오기
    @Transactional
    public BoardDto getPost(Long id) {
        // 1. 게시글 검색
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        // 2. Optional 언박싱
        BoardEntity boardEntity = boardEntityWrapper.orElse(null);
        // 3. Entity -> DTO 변환 후 리턴
        try {
            if(boardEntity==null) {
                throw new NullPointerException();
            }
            return BoardDto.builder()
                    .id(boardEntity.getId())
                    .email(boardEntity.getEmail())
                    .writer(boardEntity.getWriter())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .createdDate(boardEntity.getCreatedDate())
                    .hit(boardEntity.getHit())
                    .build();
        } catch (NullPointerException e) {
            // 게시글이 없으면 exception 처리
            throw new NotFoundPostException(ResultMessage.NOT_FOUND_POST.getValue());
        }
    }

    // 게시글 저장 프로세스
    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId(); // 데이터베이스에 저장
    }

    // 게시글 삭제 프로세스
    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    // 게시글 제목으로 검색 프로세스
    @Transactional
    public List<BoardDto> searchPost(String keyword) {
        // 1. 게시글 검색
        List<BoardEntity> boardEntities = boardRepository.findByTitleContaining(keyword); // 데이터베이스에서 찾아옴
        // 2. Entity -> DTO 변환
        List<BoardDto> boardDtoList = new ArrayList<>(); // Entity 를 Dto 에 주입시키기 위해 선언
        if (boardEntities.isEmpty()) {
            return boardDtoList;
        }
        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }
        // 3. 리턴
        return boardDtoList;
    }

    // Entity -> DTO 변환 메서드
    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        return BoardDto.builder()
                .id(boardEntity.getId())
                .email(boardEntity.getEmail())
                .writer(boardEntity.getWriter())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .createdDate(boardEntity.getCreatedDate())
                .hit(boardEntity.getHit())
                .build();
    }
}
