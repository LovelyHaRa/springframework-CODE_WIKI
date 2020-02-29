package project.code_wiki.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.code_wiki.common.PageController;
import project.code_wiki.dto.HistoryListDto;
import project.code_wiki.dto.OrderedListDto;
import project.code_wiki.dto.BarcodeDto;
import project.code_wiki.dto.WikiDto;
import project.code_wiki.security.CustomUserDetails;
import project.code_wiki.service.WikiService;

import java.util.List;
// 위키 컨트롤러
@Controller
@AllArgsConstructor
public class WikiController {

    private WikiService wikiService;
    // 메인 페이지
    @GetMapping("/")
    public String home() {
        return "redirect:/wiki/code-wiki";
    }
    @GetMapping("/wiki")
    public String wiki() {
        return "redirect:/wiki/code-wiki";
    }

    // 위키 문서 페이지
    @GetMapping("/wiki/{id}")
    public String wiki(@PathVariable("id") String id, Model model) {
        // 1. 코드 데이터 불러오기
        BarcodeDto barcodeDto = wikiService.getCode(id);
        // 2. 코드 데이터가 없으면 해당 서비스로 리다이렉트
        if(barcodeDto == null) {
            model.addAttribute("codeId", id);
            return "wiki/no-wiki-document.html";
        }
        // 3. 코드 정보를 바탕으로 최신 문서 불러오기
        Long revisionDoc = barcodeDto.getLatelyRevision();
        // 4. 데이터 전달
        WikiDto wikiDto = wikiService.getDocument(id, revisionDoc);
        model.addAttribute("wikiDto", wikiDto);
        return "wiki/wiki.html";
    }

    // 위키 문서의 리비전 페이지
    @GetMapping("/wiki/{id}/{revisionDoc}")
    public String wiki(@PathVariable("id") String id, @PathVariable("revisionDoc") Long revisionDoc, Model model) {
        // 1. 해당 리비전 문서 불러오기
        WikiDto wikiDto = wikiService.getDocument(id, revisionDoc);
        // 2. 데이터 전달
        model.addAttribute("wikiDto", wikiDto);
        return "wiki/wiki.html";
    }

    // 위키 문서 수정 페이지
    @GetMapping("/wiki/edit/{id}")
    public String editDoc(@PathVariable("id") String id, Model model) {
        // 1. 코드 데이터 불러오기
        BarcodeDto barcodeDto = wikiService.getCode(id);
        // 2. 코드 데이터가 있으면 최근 문서 데이터 전달
        if(barcodeDto != null) {
            WikiDto wikiDto = wikiService.getDocument(id, barcodeDto.getLatelyRevision());
            model.addAttribute("wikiDto", wikiDto);
        } else {
            model.addAttribute("wikiDto", null);
        }
        // 3. 코드 데이터 전달(Id)
        model.addAttribute("codeId", id);
        return "wiki/edit.html";
    }

    // 위키 문서 수정
    @PostMapping("/wiki/edit/{id}")
    public String saveDoc(@PathVariable("id")String id, WikiDto wikiDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 1. 작성자 정보 추가
        String writerId = userDetails.getUsername();
        // 2. 위키 문서 저장
        wikiService.saveDoc(id, writerId, wikiDto);
        // 3. 작성한 문서 페이지로 리다이렉트
        return "redirect:/wiki/" + id;
    }

    // 위키 문서 히스토리 리스트 페이지
    @GetMapping("/wiki/{id}/history")
    public String historyDoc(@PathVariable("id") String id, @RequestParam(value = "page", defaultValue = "1") Integer pageNum, Model model) {
        // 해당 위키 문서 리스트를 해당 페이지 영역 만큼 불러오기
        List<HistoryListDto> historyList = wikiService.getHistory(id, pageNum);
        // 페이징 구현
        Integer[] pageList = PageController.getPageList(pageNum, wikiService.getCodeDocCount(id));
        // 이전 페이지, 다음 페이지 블럭
        Integer prevBlock = pageList[0]-1;
        Integer nextBlock = pageList[pageList.length-1]+1;
        // 데이터 전달
        model.addAttribute("historyList", historyList);
        model.addAttribute("codeId", id);
        // 페이징 데이터 전달
        this.addPageAttribute(model, pageNum, pageList, prevBlock, nextBlock);
        return "wiki/wiki-history-list.html";
    }

    // 최근 변경된 문서 리스트 페이지
    @GetMapping("/wiki/list/lately")
    public String latelyList(@RequestParam(value = "page", defaultValue = "1") Integer pageNum, Model model) {
        // 1. 해당 위키 문서 리스트를 해당 페이지 영역 만큼 불러오기
        List<OrderedListDto> wikiList = wikiService.getWikiList("lately", pageNum);
        // 2. 데이터 전달
        processWikiList(pageNum, wikiList, model, "lately");
        return "wiki/wiki-list.html";
    }
    // 편집된지 오래 된 문서 리스트 페이지
    @GetMapping("/wiki/list/old")
    public String oldList(@RequestParam(value = "page", defaultValue = "1") Integer pageNum, Model model) {
        // 1. 해당 위키 문서 리스트를 해당 페이지 영역 만큼 불러오기
        List<OrderedListDto> wikiList = wikiService.getWikiList("old", pageNum);
        // 2. 데이터 전달
        processWikiList(pageNum, wikiList, model, "old");
        return "wiki/wiki-list.html";
    }
    // 내용이 긴 문서 리스트 페이지
    @GetMapping("/wiki/list/longest")
    public String longestList(@RequestParam(value = "page", defaultValue = "1") Integer pageNum, Model model) {
        // 1. 해당 위키 문서 리스트를 해당 페이지 영역 만큼 불러오기
        List<OrderedListDto> wikiList = wikiService.getWikiList("longest", pageNum);
        // 2. 데이터 전달
        processWikiList(pageNum, wikiList, model, "longest");
        return "wiki/wiki-list.html";
    }
    // 내용이 짧은 문서 리스트 페이지
    @GetMapping("/wiki/list/shortest")
    public String shortestList(@RequestParam(value = "page", defaultValue = "1") Integer pageNum, Model model) {
        // 1. 해당 위키 문서 리스트를 해당 페이지 영역 만큼 불러오기
        List<OrderedListDto> wikiList = wikiService.getWikiList("shortest", pageNum);
        // 2. 데이터 전달
        processWikiList(pageNum, wikiList, model, "shortest");
        return "wiki/wiki-list.html";
    }
    // 위키 문서 검색결과 리스트 페이지
    @GetMapping("/wiki/search")
    public String searchDoc(@RequestParam(value = "query") String query, @RequestParam(value = "page", defaultValue = "1") Integer pageNum, Model model) {
        // 1. 검색결과 문서 리스트를 해당 페이지 영역 만큼 불러오기
        List<OrderedListDto> wikiList = wikiService.searchDoc(query, pageNum);
        // 2. 데이터 전달
        processWikiList(pageNum, wikiList, model, query);
        return "wiki/search-result.html";
    }
    // 위키 문서 검색 후 바로 이동
    @GetMapping("/wiki/direct")
    public String directDoc(@RequestParam(value = "query") String query) {
        // 1. 검색결과 문서 불러오기
        String result = wikiService.searchDoc(query);
        // 2. 검색결과가 있으면 해당 문서 페이지로 리다이렉트
        if (result!=null) {
            return "redirect:/wiki/" + result;
        }
        // 3. 없으면 등록 페이지로 리다이렉트
        return "redirect:/wiki/no-wiki-document.html";
    }
    // 무작위 문서 리다이렉트
    @GetMapping("/wiki/shuffle")
    public String shuffleDoc() {
        // 1. 무작위로 문서 불러오기
        String result = wikiService.shuffleDoc();
        // 2. 해당 문서 페이지로 리다이렉트
        return "redirect:/wiki/" + result;
    }

    // 데이터 전달 공통 부분 처리
    private void processWikiList(Integer pageNum, List<OrderedListDto> wikiList, Model model, String listType) {
        // 페이징 구현
        Integer[] pageList = PageController.getPageList(pageNum, wikiService.getCodeCount());

        // 이전 페이지, 다음 페이지 블럭
        Integer prevBlock = pageList[0]-1;
        Integer nextBlock = pageList[pageList.length-1]+1;

        // 데이터 전달
        model.addAttribute("wikiList", wikiList);
        // 정렬 타입 전달
        model.addAttribute("listType", listType);
        // 페이징 관련 데이터 전달
        this.addPageAttribute(model, pageNum, pageList, prevBlock, nextBlock);
    }

    // 페이징 관련 데이터 전달, 컨트롤러 파일 내에서 모델 속성을 추가해야 타임리프가 바인딩 할 수 있다
    public void addPageAttribute(Model model, Integer pageNum, Integer[] pageList, Integer prevBlock, Integer nextBlock) {
        model.addAttribute("pageList", pageList);
        model.addAttribute("prevBlock", prevBlock>0 ? prevBlock : 0);
        model.addAttribute("nextBlock",
                nextBlock > (pageList[0]-1) + PageController.BLOCK_PAGE_NUM_COUNT ? nextBlock : 0);
        model.addAttribute("page", pageNum);
    }
}
