package project.code_wiki.common;
// 페이징 컨트롤러 클래스
public class PageController {
    public static int BLOCK_PAGE_NUM_COUNT = 5; // 한 블럭에 존재하는 페이지 번호 수
    public static int PAGE_POST_COUNT = 5; // 한 페이지에 존재하는 게시글 수
    // 페이지 범위를 배열에 저장해서 리턴하는 메서드
    public static Integer[] getPageList(Integer curPageNum, Long dataCount) {
        // 총 게시글 갯수
        Double postsTotalCount = Double.valueOf(dataCount);

        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산 (올림으로 계산)
        int totalLastPageNum = (int)(Math.ceil((postsTotalCount/ PageController.PAGE_POST_COUNT)));

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산(사용안함)
//        Integer blockLastPageNum = Math.min(totalLastPageNum, curPageNum + PageController.BLOCK_PAGE_NUM_COUNT);

        // 페이지 번호 할당
        // 현재 블럭 위치 계산 ( ex) 1~5 : 0그룹 , 6~10: 1그룹)
        int curBlock = (int)Math.floor((curPageNum-1)/ (double)PageController.PAGE_POST_COUNT);
        // 페이지 목록 시작 번호
        int startBlock = curBlock * PageController.BLOCK_PAGE_NUM_COUNT + 1;
        // 페이지 목록 끝 번호
        int lastBlock = Math.min((startBlock + PageController.BLOCK_PAGE_NUM_COUNT - 1), totalLastPageNum);
        // 페이지 목록 사이즈, 게시물 총 갯수의 마지막 번호까지를 범위로 한다
        int blockSize = (lastBlock % PageController.BLOCK_PAGE_NUM_COUNT)==0
                ? PageController.BLOCK_PAGE_NUM_COUNT
                : (lastBlock % PageController.BLOCK_PAGE_NUM_COUNT);
        // 페이지 번호 저장
        Integer[] pageList = new Integer[blockSize];
        for (int value = startBlock, i=0; value <= lastBlock && i<blockSize; value++, i++) {
            pageList[i] = value;
        }

        return pageList;
    }
}
