package com.teamsupercat.roupangbackend.common;

import com.teamsupercat.roupangbackend.common.dynamicMessageException.CustomMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Global error controller
@RestControllerAdvice   //@ExceptionHandler,@ModelAttribute, @InitBinder가 적용된 메서드에 AOP 적용
public class CustomExceptionHandler {
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<?> handleApiRequestException(CustomException ex) {

        HttpStatus httpStatus = ex.getErrorCode().getHttpStatus();
        String errMSG = ex.getErrorCode().getErrorMsg();

        return new ResponseEntity<>(ResponseDto.fail(errMSG), httpStatus);
    }

    @ExceptionHandler(value = {CustomMessageException.class})
    public ResponseEntity<?> handMessageRequestException(CustomMessageException ex) {

        HttpStatus httpStatus = ex.getErrorCode().getHttpStatus();
        String errMSG = ex.getErrorCode().getErrorMsg();
        String dynamicMSG = ex.getDynamicMessage();
        return new ResponseEntity<>(ResponseDto.fail(dynamicMSG + errMSG), httpStatus);
    }
}
