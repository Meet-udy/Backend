package com.api.meetudy.oauth.service;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.oauth.dto.KakaoTokenDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ResponseEntity<String> requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("code", code);
        params.set("client_id", clientId);
        params.set("client_secret", clientSecret);
        params.set("redirect_uri", redirectUri);
        params.set("grant_type", grantType);

        URI uri = UriComponentsBuilder
                .fromUriString(tokenUri)
                .queryParams(params)
                .encode().build().toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    public KakaoTokenDto getAccessToken(ResponseEntity<String> response) {
        try {
            log.info("response.getBody() : {}", response.getBody());
            return objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error while processing JSON: {}", e.getMessage());
            return null;
        }
    }

    public ResponseEntity<String> requestUserInfo(KakaoTokenDto accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken.getAccess_token());
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        URI uri = UriComponentsBuilder
                .fromUriString(userInfoUri)
                .encode().build().toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    public Member getUserInfo(ResponseEntity<String> userInfo) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(Objects.requireNonNull(userInfo.getBody()));
        JSONObject member = (JSONObject) jsonObject.get("kakao_account");

        return Member.builder()
                .email((String) member.get("email"))
                .nickname((String) member.get("nickname"))
                .profileImage((String) member.get("profile_image"))
                .build();
    }

}