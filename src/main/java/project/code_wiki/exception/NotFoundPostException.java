package project.code_wiki.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
// 게시글을 찾을 수 없을 때 발생하는 exception
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NotFoundPostException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotFoundPostException(String message) {
        super(message);
    }
}
