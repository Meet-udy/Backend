package com.api.meetudy.study.group.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.global.utils.LeaderAccessValidator;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.dto.StudyGroupApplicantDto;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.enums.GroupMemberStatus;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.GroupMemberRepository;
import com.api.meetudy.study.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupManagementService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;
    private final LeaderAccessValidator leaderAccessValidator;

    @Transactional
    public String createStudyGroup(StudyGroupDto studyGroupDto, Member member) {
        StudyGroup studyGroup = studyGroupMapper.toStudyGroup(studyGroupDto, member);

        groupRepository.save(studyGroup);

        StudyGroupMember leaderMember = new StudyGroupMember(null, GroupMemberStatus.LEADER, member, studyGroup);
        groupMemberRepository.save(leaderMember);

        member.updateActivityScore(member.getActivityScore() + 8);

        return "Study group has been successfully created.";
    }

    @Transactional
    public String approveJoinRequest(Long groupMemberId, Member member) {
        StudyGroupMember newMember = groupMemberRepository.findById(groupMemberId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_MEMBER_NOT_FOUND));

        StudyGroup studyGroup = newMember.getStudyGroup();

        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        if (studyGroup.getMembers().size() >= studyGroup.getMaxParticipants()) {
            throw new CustomException(ErrorStatus.MAX_PARTICIPANTS_EXCEEDED);
        }

        newMember.updateStatus(GroupMemberStatus.MEMBER);

        Member groupMember = newMember.getMember();
        groupMember.updateActivityScore(groupMember.getActivityScore() + 5);

        return "The join request has been approved.";
    }

    @Transactional
    public String rejectJoinRequest(Long groupMemberId, Member member) {
        StudyGroupMember groupMember = groupMemberRepository.findById(groupMemberId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_MEMBER_NOT_FOUND));

        StudyGroup studyGroup = groupMember.getStudyGroup();

        leaderAccessValidator.checkLeaderAccess(member, studyGroup);
        groupMemberRepository.delete(groupMember);

        return "The join request has been rejected.";
    }

    @Transactional
    public String closeRecruitment(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        studyGroup.closeRecruitment();
        groupRepository.save(studyGroup);

        return "Recruitment has been closed.";
    }

    @Transactional(readOnly = true)
    public List<StudyGroupApplicantDto> getJoinRequests(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        List<StudyGroupMember> pendingMembers = groupMemberRepository.findByStudyGroupAndStatus(studyGroup, GroupMemberStatus.REQUESTED);

        return studyGroupMapper.toStudyGroupApplicantDtoList(pendingMembers);
    }

}