package com.api.meetudy.member.service;

import com.api.meetudy.auth.dto.JwtTokenDto;
import com.api.meetudy.auth.provider.JwtTokenProvider;
import com.api.meetudy.auth.service.TokenService;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.dto.AdditionalInfoDto;
import com.api.meetudy.member.dto.KakaoLoginDto;
import com.api.meetudy.member.dto.SignInDto;
import com.api.meetudy.member.dto.SignUpDto;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.enums.LoginType;
import com.api.meetudy.member.mapper.MemberMapper;
import com.api.meetudy.member.repository.MemberRepository;
import com.api.meetudy.oauth.dto.KakaoTokenDto;
import com.api.meetudy.oauth.service.KakaoLoginService;
import com.api.meetudy.interest.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final MemberMapper memberMapper;
    private final TokenService tokenService;
    private final KakaoLoginService kakaoLoginService;
    private final InterestService interestService;

    @Transactional
    public String signUp(SignUpDto signUpDto) {
        if(memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new CustomException(ErrorStatus.USERNAME_ALREADY_EXISTS);
        }

        Member member = memberMapper.toMember(signUpDto);
        member.updatePassword(passwordEncoder.encode(signUpDto.getPassword()));
        member.updateLoginType(LoginType.JWT);

        interestService.setMemberInterests(signUpDto.getStudyCategories(), member);

        memberRepository.save(member);

        return "Sign-up completed successfully.";
    }

    public JwtTokenDto signIn(SignInDto signInDto) {
        Member member = memberRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorStatus.PASSWORD_MISMATCH);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                signInDto.getUsername(), signInDto.getPassword());
        Authentication authentication = authenticate(authenticationToken);

        JwtTokenDto jwtToken = tokenProvider.generateToken(authentication);
        tokenService.storeRefreshToken(authentication.getName(), jwtToken);

        return jwtToken;
    }

    @Transactional
    public KakaoLoginDto signInWithKakao(String code) {
        ResponseEntity<String> accessToken = kakaoLoginService.requestAccessToken(code);
        KakaoTokenDto kakaoToken = kakaoLoginService.getAccessToken(accessToken);

        ResponseEntity<String> userInfo = kakaoLoginService.requestUserInfo(kakaoToken);
        Member member = kakaoLoginService.getUserInfo(userInfo);
        member.updateUsername(member.getEmail());

        Member existMember = memberRepository.findByEmail(member.getEmail()).orElse(null);
        Boolean isFirstLogin = false;

        if(existMember == null) {
            member.updateLoginType(LoginType.KAKAO);
            member.updateRoles(Collections.singletonList("ROLE_USER"));
            memberRepository.save(member);

            isFirstLogin = true;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getEmail(), null,
                        member.getRoles().stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()));

        JwtTokenDto tokenDto = tokenProvider.generateToken(authenticationToken);
        tokenService.storeRefreshToken(member.getEmail(), tokenDto);

        return memberMapper.toKakaoLoginDto(tokenDto, isFirstLogin);
    }

    @Transactional
    public String updateAdditionalInfo(AdditionalInfoDto additionalInfoDto, Member member) {
        member.updateAdditionalInfo(additionalInfoDto);
        interestService.setMemberInterests(additionalInfoDto.getStudyCategories(), member);

        memberRepository.save(member);

        return "Additional information updated successfully.";
    }

    @Transactional
    public String logout(String accessToken) {
        tokenProvider.validateToken(accessToken);

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        tokenService.deleteRefreshToken(authentication.getName());
        tokenService.storeLogoutToken(accessToken);

        return "You have been logged out.";
    }

    public JwtTokenDto refreshToken(String accessToken, String refreshToken) {
        tokenProvider.validateToken(accessToken);
        tokenProvider.validateToken(refreshToken);

        tokenService.checkIfRefreshTokenIsLoggedOut(refreshToken);

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        JwtTokenDto jwtToken = tokenProvider.generateToken(authentication);

        tokenService.storeRefreshToken(authentication.getName(), jwtToken);

        return jwtToken;
    }

    private Authentication authenticate(UsernamePasswordAuthenticationToken authenticationToken) {
        return authenticationManager.authenticate(authenticationToken);
    }

}