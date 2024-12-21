package com.api.meetudy.study.group.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.enums.GroupMemberStatus;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.StudyGroupMemberRepository;
import com.api.meetudy.study.group.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupManagementService {

    private final StudyGroupRepository groupRepository;
    private final StudyGroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;

    @Transactional
    public String createStudyGroup(StudyGroupDto studyGroupDto, Member member) {
        StudyGroup studyGroup = studyGroupMapper.toStudyGroup(studyGroupDto, member);

        member.updateActivityScore(member.getActivityScore() + 8);
        groupRepository.save(studyGroup);

        return "Study group has been successfully created.";
    }

    @Transactional
    public String approveJoinRequest(Long groupMemberId) {
        StudyGroupMember groupMember = groupMemberRepository.findById(groupMemberId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_MEMBER_NOT_FOUND));

        StudyGroup studyGroup = groupMember.getStudyGroup();

        if (studyGroup.getMembers().size() >= studyGroup.getMaxParticipants()) {
            throw new CustomException(ErrorStatus.MAX_PARTICIPANTS_EXCEEDED);
        }

        groupMember.updateStatus(GroupMemberStatus.ACCEPTED);

        Member member = groupMember.getMember();
        member.updateActivityScore(member.getActivityScore() + 5);

        return "The join request has been approved.";
    }

    @Transactional
    public String rejectJoinRequest(Long groupMemberId) {
        StudyGroupMember groupMember = groupMemberRepository.findById(groupMemberId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_MEMBER_NOT_FOUND));

        groupMemberRepository.delete(groupMember);

        return "The join request has been rejected.";
    }

}