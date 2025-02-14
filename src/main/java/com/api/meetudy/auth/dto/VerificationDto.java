package com.api.meetudy.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VerificationDto {

    private String email;

    private String code;

}