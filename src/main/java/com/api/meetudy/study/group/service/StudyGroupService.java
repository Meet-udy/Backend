package com.api.meetudy.study.group.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.GroupMemberRepository;
import com.api.meetudy.study.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupJoinService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
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

}