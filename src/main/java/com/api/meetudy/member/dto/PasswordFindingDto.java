package com.api.meetudy.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordFindingDto {

    @NotBlank
    private String username;

    @Email
    private String email;

}
