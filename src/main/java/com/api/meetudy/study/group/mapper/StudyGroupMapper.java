package com.api.meetudy.study.group.mapper;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.dto.StudyGroupApplicantDto;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StudyGroupMapper {

    @Mapping(target = "id", ignore = true)
    StudyGroupMember toStudyGroupMember(StudyGroup studyGroup, Member member);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "leader", source = "leader")
    @Mapping(target = "members", ignore = true)
    StudyGroup toStudyGroup(StudyGroupDto dto, Member leader);

    @Mapping(source = "member.nickname", target = "nickname")
    @Mapping(source = "member.major", target = "major")
    @Mapping(source = "member.introduction", target = "introduction")
    @Mapping(source = "member.activityScore", target = "activityScore")
    StudyGroupApplicantDto toStudyGroupApplicantDto(StudyGroupMember studyGroupMember);

    StudyGroupDto toStudyGroupDto(StudyGroup studyGroup);

    @IterableMapping(elementTargetType = StudyGroupApplicantDto.class)
    List<StudyGroupApplicantDto> toStudyGroupApplicantDtoList(List<StudyGroupMember> studyGroupMembers);

    @IterableMapping(elementTargetType = StudyGroupDto.class)
    List<StudyGroupDto> toStudyGroupDtoList(List<StudyGroup> studyGroups);

    default List<StudyGroupDto> toStudyGroupDtoListFromMembers(List<StudyGroupMember> studyGroupMembers) {
        return studyGroupMembers.stream()
                .map(StudyGroupMember::getStudyGroup)
                .map(this::toStudyGroupDto)
                .collect(Collectors.toList());
    }

}