package com.api.meetudy.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UsernameDuplicationDto {

    @NotBlank
    private String username;

}