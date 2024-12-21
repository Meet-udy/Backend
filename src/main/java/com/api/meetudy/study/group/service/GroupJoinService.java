package com.api.meetudy.study.group.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.dto.StudyGroupApplicantDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.enums.GroupMemberStatus;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.StudyGroupMemberRepository;
import com.api.meetudy.study.group.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupJoinService {

    private final StudyGroupRepository groupRepository;
    private final StudyGroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;

    @Transactional
    public String requestJoinGroup(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        boolean isAlreadyRequested = groupMemberRepository.existsByStudyGroupAndMember(studyGroup, member);
        if (isAlreadyRequested) {
            throw new CustomException(ErrorStatus.REQUEST_ALREADY_SENT);
        }

        StudyGroupMember studyGroupMember = studyGroupMapper.toStudyGroupMember(studyGroup, member);
        groupMemberRepository.save(studyGroupMember);

        return "Join request submitted successfully";
    }

    @Transactional(readOnly = true)
    public List<StudyGroupApplicantDto> getJoinRequests(Long groupId) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        List<StudyGroupMember> pendingMembers = groupMemberRepository.findByStudyGroupAndStatus(studyGroup, GroupMemberStatus.PENDING);

        return studyGroupMapper.toStudyGroupApplicantDtoList(pendingMembers);
    }

}