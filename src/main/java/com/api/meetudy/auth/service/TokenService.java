package com.api.meetudy.auth.service;

import com.api.meetudy.auth.dto.JwtTokenDto;
import com.api.meetudy.auth.provider.JwtTokenProvider;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider tokenProvider;

    public static final String RT_PREFIX = "RT:";
    public static final String LOGOUT_KEY = "logout";

    public void storeRefreshToken(String username, JwtTokenDto jwtToken) {
        redisTemplate.opsForValue().set(RT_PREFIX + username, jwtToken.getRefreshToken(),
                jwtToken.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String username) {
        if (redisTemplate.opsForValue().get(RT_PREFIX + username) != null) {
            redisTemplate.delete(RT_PREFIX + username);
        }
    }

    public void storeLogoutToken(String accessToken) {
        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, LOGOUT_KEY, expiration, TimeUnit.MILLISECONDS);
    }

    public void checkIfRefreshTokenIsLoggedOut(String refreshToken) {
        String isLogout = (String) redisTemplate.opsForValue().get(refreshToken);
        if (isLogout != null) {
            throw new CustomException(ErrorStatus.REFRESH_TOKEN_LOGGED_OUT);
        }
    }

}
