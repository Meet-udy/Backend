package com.api.meetudy.member.mapper;

import com.api.meetudy.member.dto.SignUpDto;
import com.api.meetudy.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;

@Mapper(componentModel = "spring", imports = Collections.class)
public interface MemberMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", expression = "java(Collections.singletonList(\"ROLE_USER\"))")
    Member toMember(SignUpDto signUpDto);

}
