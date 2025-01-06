package com.api.meetudy.mypage.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.dto.MemberDto;
import com.api.meetudy.member.dto.MemberUpdateDto;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.mapper.MemberMapper;
import com.api.meetudy.member.service.MemberService;
import com.api.meetudy.study.group.dto.StudyGroupUpdateDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.enums.GroupMemberStatus;
import com.api.meetudy.study.group.enums.StudyCategory;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.GroupMemberRepository;
import com.api.meetudy.study.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberMapper memberMapper;
    private final StudyGroupMapper groupMapper;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberDto getMemberProfile(Member member) {
        List<StudyCategory> interests = member.getMemberInterests().stream()
                .map(memberInterest -> memberInterest.getInterest().getStudyCategory())
                .collect(Collectors.toList());

        MemberDto memberDto = memberMapper.toMemberDto(member);
        memberDto.setInterests(interests);

        return memberDto;
    }

    @Transactional
    public String updateMember(MemberUpdateDto updateMemberDto, Member member) {
        memberMapper.updateMemberFromDto(updateMemberDto, member);

        if(updateMemberDto.getPassword() != null) {
            member.updatePassword(passwordEncoder.encode(updateMemberDto.getPassword()));
        }

        if(updateMemberDto.getInterests() != null) {
            List<StudyCategory> interests = updateMemberDto.getInterests();
            memberService.setMemberInterests(interests, member);
        }

        return "User information has been updated.";
    }

    @Transactional
    public String updateGroupInfo(Long groupId, StudyGroupUpdateDto groupUpdateDto, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        checkLeaderAccess(member, studyGroup);

        groupMapper.updateStudyGroupFromDto(groupUpdateDto, studyGroup);

        return "Study group information has been updated.";
    }

    @Transactional
    public String removeMember(Long groupId, Long memberId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        checkLeaderAccess(member, studyGroup);

        StudyGroupMember groupMember = groupMemberRepository.findByMemberIdAndStudyGroupId(memberId, groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NO_SUCH_MEMBER_IN_STUDY_GROUP));

        groupMemberRepository.delete(groupMember);

        return "The member has been removed from the group.";
    }

    @Transactional
    public String delegateLeader(Long groupId, Long newLeaderId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        checkLeaderAccess(member, studyGroup);

        StudyGroupMember newLeader = groupMemberRepository.findByMemberIdAndStudyGroupId(newLeaderId, groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NO_SUCH_MEMBER_IN_STUDY_GROUP));

        Member previousLeader = studyGroup.getLeader();

        StudyGroupMember previousLeaderMember = StudyGroupMember.builder()
                .member(previousLeader)
                .studyGroup(studyGroup)
                .status(GroupMemberStatus.ACCEPTED)
                .build();

        groupMemberRepository.save(previousLeaderMember);
        groupMemberRepository.delete(newLeader);

        studyGroup.updateLeader(newLeader.getMember());

        return "Leadership has been delegated to the member.";
    }


    @Transactional
    public String leaveGroup(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        if (studyGroup.getLeader().getId().equals(member.getId())) {
            throw new CustomException(ErrorStatus.CANNOT_LEAVE_THE_GROUP);
        }

        StudyGroupMember groupMember = groupMemberRepository.findByMemberIdAndStudyGroupId(member.getId(), groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NO_SUCH_MEMBER_IN_STUDY_GROUP));

        groupMemberRepository.delete(groupMember);

        return "You have successfully left the group.";
    }

    private void checkLeaderAccess(Member member, StudyGroup studyGroup) {
        if (!studyGroup.getLeader().getId().equals(member.getId())) {
            throw new CustomException(ErrorStatus.GROUP_LEADER_ACCESS_ONLY);
        }
    }

}