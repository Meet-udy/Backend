package com.api.meetudy.member.dto;

import com.api.meetudy.auth.dto.JwtTokenDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoLoginDto {

    private JwtTokenDto jwtTokenDto;

    private Boolean isFirstLogin;

}