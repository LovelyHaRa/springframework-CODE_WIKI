package project.code_wiki.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
// 사용자에게 표시할 메시지를 저장
@AllArgsConstructor
@Getter
public enum ResultMessage {
    
    NOT_MATCH_PASSWORD("기존 비밀번호가 일치하지 않습니다."),
    NOT_EQUAL_NEW_PASSWORD("비밀번호와 확인이 일치하지 않습니다."),
    SUCCESS_UPDATE_USER("성공적으로 회원정보기 수정되었습니다."),
    NOT_EXIST_EMAIL("등록된 이메일이 아닙니다."),
    NOT_MATCH_USERNAME("사용자 이름이 일치하지 않습니다."),
    EDIT_POST_ACCESS_DENIED("작성자만 접근할 수 있습니다."),
    NOT_EQUAL_EMAIL_OR_PASSWORD("이메일 또는 비밀번호가 일치하지 않습니다."),
    NOT_FOUND_POST("수정하려는 게시글을 찾지 못함"),
    NOT_EXIST_VARCODE_ID("일치하는 바코드 없음");

    private String value;
}
