package com.api.meetudy.member.controller;

import com.api.meetudy.auth.dto.JwtTokenDto;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.member.dto.SignInDto;
import com.api.meetudy.member.dto.SignUpDto;
import com.api.meetudy.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_PARAM = "refreshToken";
    public static final String BEARER_PREFIX = "Bearer ";

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        String message = memberService.signUp(signUpDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<JwtTokenDto>> signIn(@Valid @RequestBody SignInDto signInDto) {
        JwtTokenDto jwtTokenDto = memberService.signIn(signInDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(jwtTokenDto));
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<ApiResponse<JwtTokenDto>> kakaoLogin(@RequestParam String code) {
        JwtTokenDto jwtTokenDto = memberService.signInWithKakao(code);
        return ResponseEntity.ok(ApiResponse.onSuccess(jwtTokenDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader(AUTHORIZATION_HEADER) String accessToken) {
        String token = accessToken.replace(BEARER_PREFIX, "");
        String message = memberService.logout(token);

        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtTokenDto>> refreshToken(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                                 @RequestParam(REFRESH_TOKEN_PARAM) String refreshToken) {
        String token = accessToken.replace(BEARER_PREFIX, "");
        JwtTokenDto jwtTokenDto = memberService.refreshToken(token, refreshToken);

        return ResponseEntity.ok(ApiResponse.onSuccess(jwtTokenDto));
    }

}