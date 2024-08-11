package bigbrother.slimdealz.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public CustomErrorResponse handleCustomException(CustomException e, HttpServletRequest request) {
        log.error("error code : {}, url {}, message: {}",
                e.getCustomErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return CustomErrorResponse.builder()
                .code(e.getCustomErrorCode())
                .message(e.getDetailMessage())
                .build();
    }
}
