package com.api.meetudy.member.mapper;

import com.api.meetudy.auth.dto.JwtTokenDto;
import com.api.meetudy.member.dto.KakaoLoginDto;
import com.api.meetudy.member.dto.MemberDto;
import com.api.meetudy.member.dto.SignUpDto;
import com.api.meetudy.member.dto.MemberUpdateDto;
import com.api.meetudy.member.entity.Member;
import org.mapstruct.*;

import java.util.Collections;

@Mapper(componentModel = "spring", imports = Collections.class)
public interface MemberMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", expression = "java(Collections.singletonList(\"ROLE_USER\"))")
    Member toMember(SignUpDto signUpDto);

    KakaoLoginDto toKakaoLoginDto(JwtTokenDto jwtTokenDto, Boolean isFirstLogin);

    @Mapping(target = "interests", ignore = true)
    MemberDto toMemberDto(Member member);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "memberInterests", ignore = true)
    void updateMemberFromDto(MemberUpdateDto dto, @MappingTarget Member member);

}