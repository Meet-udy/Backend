package com.api.meetudy.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailDto {

    @Email
    private String email;

}