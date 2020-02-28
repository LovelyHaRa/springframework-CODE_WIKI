package project.code_wiki.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.code_wiki.common.PageController;
import project.code_wiki.common.ResultMessage;
import project.code_wiki.dto.BoardDto;
import project.code_wiki.exception.AccessDeniedException;
import project.code_wiki.security.CustomUserDetails;
import project.code_wiki.service.BoardService;

import java.util.List;
// 게시판 컨트롤러
@Controller
@AllArgsConstructor
public class BoardController {

    private BoardService boardService; // 게시판의 각종 기능 요청을 처리하는 객체

    // 게시글 목록 view 연결
    // Model 객체를 통해 데이터 전달
    @GetMapping("/board")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        // 1. 게시글 요청(페이지에 해당하는 개수만 요청)
        List<BoardDto> postList = boardService.getPostList(pageNum);
        // 2. 게시글의 유무에 따른 처리
        if (boardService.getPostCount()==0) {
            model.addAttribute("state", "noContent");
        } else {
            // 페이징 구현
            Integer[] pageList = PageController.getPageList(pageNum, boardService.getPostCount());
            // 이전 페이지, 다음 페이지 블럭
            Integer prevBlock = pageList[0] - 1;
            Integer nextBlock = pageList[pageList.length - 1] + 1;
            // Model 인스턴스에 데이터 저장
            model.addAttribute("postList", postList);
            model.addAttribute("state", "OK");
            this.addPageAttribute(model, pageNum, pageList, prevBlock, nextBlock);
        }
        // 페이지 리턴
        return "board/board.html";
    }

    // 게시글 쓰기 view 연결
    @GetMapping("/board/write")
    public String write() {
        return "board/write.html";
    }

    // 게시글 저장 로직
    @PostMapping("/board/write")
    public String write(BoardDto boardDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 1. 작성자 정보 저장 및 조회수 초기화
        boardDto.setEmail(userDetails.getUsername());
        boardDto.setWriter(userDetails.getName());
        boardDto.setHit(0L);
        // 2. 게시글 데이터베이스에 저장
        long postId = boardService.savePost(boardDto);
        // 3. 작성한 게시글 페이지로 이동
        String redirectPath = "/board/post/"+postId;
        return "redirect:/"+redirectPath;
    }

    // 게시글 상세보기
    @GetMapping("/board/post/{id}")
    public String viewPost(@PathVariable("id")Long id, Model model) {
        // 1. 게시글 정보 불러오기
        BoardDto boardDto = boardService.getPost(id);
        // 2. 조회수 증가
        boardDto.setHit(boardDto.getHit()+1);
        boardService.savePost(boardDto);
        // 3. 데이터 전달
        model.addAttribute("boardDto", boardDto);
        return "board/post.html";
    }

    // 게시글 수정 view
    @GetMapping("board/post/edit/{id}")
    public String editPost(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 1. 게시글 정보 불러오기
        BoardDto boardDto = boardService.getPost(id);
        // 2. 작성자가 아닌 유저는 접근 거부 처리
        if(!boardDto.getEmail().equals(userDetails.getUsername())) {
            throw new AccessDeniedException(ResultMessage.EDIT_POST_ACCESS_DENIED.getValue());
        }
        // 3. 데이터 전달
        model.addAttribute("boardDto", boardDto);
        return "board/modify.html";
    }

    // Restful API 사용, 게시글 수정 처리
    @PutMapping("board/post/edit/{id}")
    public String updatePost(BoardDto boardDto) {
        // 1. 게시글 수정
        boardService.savePost(boardDto);
        // 2. 수정된 게시글 페이지로 이동
        String redirectPath = "/board/post/{id}";
        return "redirect:" + redirectPath;
    }

    // Restful API 사용, 게시글 삭제 처리
    @DeleteMapping("/board/post/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        // 1. 게시글 삭제
        boardService.deletePost(id);
        // 2. 게시판 목록으로 이동
        return "redirect:/board";
    }

    // 게시글 검색(제목으로 검색)
    @GetMapping("/board/search")
    public String searchPost(@RequestParam(value = "keyword") String keyword, Model model) {
        // 1. 데이터 검색
        List<BoardDto> postList = boardService.searchPost(keyword);
        // 2. 결과 리스트 데이터 전달
        model.addAttribute("postList", postList);
        return "board/board.html";
    }

    // 페이지 속성 전달. 컨트롤러 파일 내에서 모델 속성을 추가해야 타임리프가 바인딩 할 수 있다.
    public void addPageAttribute(Model model, Integer pageNum, Integer[] pageList, Integer prevBlock, Integer nextBlock) {
        model.addAttribute("pageList", pageList);
        model.addAttribute("prevBlock", prevBlock>0 ? prevBlock : 0);
        model.addAttribute("nextBlock",
                nextBlock > (pageList[0]-1) + PageController.BLOCK_PAGE_NUM_COUNT ? nextBlock : 0);
        model.addAttribute("page", pageNum);
    }
}
