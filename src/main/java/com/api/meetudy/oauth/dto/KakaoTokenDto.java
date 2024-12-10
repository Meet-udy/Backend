package com.api.meetudy.oauth.dto;

import lombok.Getter;

@Getter
public class KakaoTokenDto {

    private String id_token;

    private String token_type;

    private String access_token;

    private String refresh_token;

    private String expires_in;

    private int refresh_token_expires_in;

    private String scope;

}