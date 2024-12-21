package com.api.meetudy.study.group.service;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.StudyGroupMemberRepository;
import com.api.meetudy.study.group.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupRetrievalService {

    private final StudyGroupRepository groupRepository;
    private final StudyGroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;

    @Transactional(readOnly = true)
    public List<StudyGroupDto> getCreatedStudyGroups(Member member) {
        List<StudyGroup> createdStudyGroups = groupRepository.findByLeader(member);
        return studyGroupMapper.toStudyGroupDtoList(createdStudyGroups);
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> getJoinedStudyGroups(Member member) {
        List<StudyGroupMember> groupMembers = groupMemberRepository.findByMember(member);
        return studyGroupMapper.toStudyGroupDtoListFromMembers(groupMembers);
    }

}