package com.api.meetudy.global.response.exception;

import com.api.meetudy.global.response.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorStatus errorStatus;

}