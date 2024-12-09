package com.api.meetudy.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}