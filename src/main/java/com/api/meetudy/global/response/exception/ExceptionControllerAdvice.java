package com.api.meetudy.global.response.exception;

import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.global.response.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException e) {
        ErrorStatus errorStatus = e.getErrorStatus();

        return ResponseEntity.status(errorStatus.getHttpStatus()).body(ApiResponse.onFailure(
                errorStatus.getCode(), errorStatus.getMessage(), null));
    }

}