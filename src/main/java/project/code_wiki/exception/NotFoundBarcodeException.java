package project.code_wiki.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
// 코드 정보를 찾을 수 없을 때 발생하는 exception
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NotFoundBarcodeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotFoundBarcodeException(String message) {
        super(message);
    }
}
