package com.sii.siipirog.exception;

import com.sii.siipirog.exception.dto.ErrorMessageExceptionDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ErrorMessageException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageExceptionDto handleGameLogicException(ErrorMessageException ex) {
        String msg = ex.getMessage();
        log.warn("Error message: {}", msg);
        return ErrorMessageExceptionDto.builder()
                .message(msg)
                .build();
    }
}
