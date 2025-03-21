package com.api.meetudy.global.init;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.enums.LoginType;
import com.api.meetudy.member.repository.MemberRepository;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.enums.GroupMemberStatus;
import com.api.meetudy.study.group.enums.Location;
import com.api.meetudy.study.group.enums.StudyCategory;
import com.api.meetudy.study.group.repository.GroupMemberRepository;
import com.api.meetudy.study.group.repository.GroupRepository;
import com.api.meetudy.interest.entity.Interest;
import com.api.meetudy.interest.entity.MemberInterest;
import com.api.meetudy.interest.repository.InterestRepository;
import com.api.meetudy.interest.repository.MemberInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final InterestRepository interestRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Override
    public void run(String... args) throws Exception {
        Member member1 = Member.builder()
                .email("test1@naver.com")
                .username("test123")
                .password(passwordEncoder.encode("Test12345!"))
                .nickname("test1")
                .major("컴퓨터공학과")
                .introduction("안녕하세요, 컴퓨터공학과 재학생입니다!")
                .isOnline(false)
                .location(Location.SEOUL)
                .loginType(LoginType.JWT)
                .roles(List.of("ROLE_USER"))
                .build();

        Member member2 = Member.builder()
                .email("test2@naver.com")
                .username("test1234")
                .password(passwordEncoder.encode("Test12345!"))
                .nickname("test2")
                .major("영어영문학과")
                .introduction("안녕하세요, 영어영문학과 재학생입니다!")
                .isOnline(true)
                .location(Location.GYEONGGIDO)
                .loginType(LoginType.JWT)
                .roles(List.of("ROLE_USER"))
                .build();

        Interest interest1 = Interest.builder()
                .studyCategory(StudyCategory.CERTIFICATION)
                .build();

        Interest interest2 = Interest.builder()
                .studyCategory(StudyCategory.PROGRAMMING)
                .build();

        Interest interest3 = Interest.builder()
                .studyCategory(StudyCategory.CAREER)
                .build();

        Interest interest4 = Interest.builder()
                .studyCategory(StudyCategory.LANGUAGE)
                .build();

        MemberInterest memberInterest1 = MemberInterest.builder()
                .member(member1)
                .interest(interest1)
                .build();

        MemberInterest memberInterest2 = MemberInterest.builder()
                .member(member1)
                .interest(interest2)
                .build();

        MemberInterest memberInterest3 = MemberInterest.builder()
                .member(member1)
                .interest(interest3)
                .build();

        MemberInterest memberInterest4 = MemberInterest.builder()
                .member(member2)
                .interest(interest2)
                .build();

        MemberInterest memberInterest5 = MemberInterest.builder()
                .member(member2)
                .interest(interest4)
                .build();

        StudyGroup studyGroup1 = StudyGroup.builder()
                .name("코테 스터디")
                .description("코딩테스트 준비를 위한 스터디입니다.")
                .duration("2024-12-25 ~ 2025-02-31")
                .maxParticipants(5)
                .isOnline(true)
                .method("온라인으로 진행하며, 각자 코드를 깃허브에 올려 코드 리뷰를 진행합니다.")
                .category(StudyCategory.PROGRAMMING)
                .location(Location.OTHERS)
                .build();

        StudyGroup studyGroup2 = StudyGroup.builder()
                .name("토익 스터디")
                .description("토익 준비를 위한 스터디입니다.")
                .duration("2024-12-25 ~ 2025-02-31")
                .maxParticipants(5)
                .isOnline(true)
                .method("온라인으로 진행합니다.")
                .category(StudyCategory.LANGUAGE)
                .location(Location.GYEONGGIDO)
                .build();

        StudyGroup studyGroup3 = StudyGroup.builder()
                .name("정처기 스터디")
                .description("정보처리기사 준비를 위한 스터디입니다.")
                .duration("2024-12-25 ~ 2025-02-31")
                .maxParticipants(5)
                .isOnline(false)
                .method("오프라인으로 경기도 스터디 카페에서 진행합니다.")
                .category(StudyCategory.CERTIFICATION)
                .location(Location.GYEONGGIDO)
                .build();

        StudyGroup studyGroup4 = StudyGroup.builder()
                .name("취준 스터디")
                .description("취업 준비를 위한 스터디입니다.")
                .duration("2024-12-25 ~ 2025-02-31")
                .maxParticipants(5)
                .isOnline(false)
                .method("오프라인으로 서울 스터디 카페에서 진행합니다.")
                .category(StudyCategory.CAREER)
                .location(Location.SEOUL)
                .build();

        StudyGroup studyGroup5 = StudyGroup.builder()
                .name("사회과학 스터디")
                .description("사회과학 스터디입니다.")
                .duration("2025-03-01 ~ 2025-05-01")
                .maxParticipants(10)
                .isOnline(true)
                .method("온라인 ZOOM으로 진행합니다.")
                .category(StudyCategory.SOCIAL_SCIENCES)
                .location(Location.OTHERS)
                .build();

        StudyGroup studyGroup6 = StudyGroup.builder()
                .name("피그마 스터디")
                .description("피그마 스터디입니다.")
                .duration("2025-03-01 ~ 2025-05-01")
                .maxParticipants(10)
                .isOnline(true)
                .method("온라인 ZOOM으로 진행합니다.")
                .category(StudyCategory.DESIGN)
                .location(Location.OTHERS)
                .build();

        StudyGroupMember groupMember1 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member1)
                .studyGroup(studyGroup1)
                .build();

        StudyGroupMember groupMember2 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member2)
                .studyGroup(studyGroup1)
                .build();

        StudyGroupMember groupMember3 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member1)
                .studyGroup(studyGroup2)
                .build();

        StudyGroupMember groupMember4 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member2)
                .studyGroup(studyGroup2)
                .build();

        StudyGroupMember groupMember5 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member2)
                .studyGroup(studyGroup3)
                .build();

        StudyGroupMember groupMember6 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member1)
                .studyGroup(studyGroup3)
                .build();

        StudyGroupMember groupMember7 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member2)
                .studyGroup(studyGroup4)
                .build();

        StudyGroupMember groupMember8 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member1)
                .studyGroup(studyGroup4)
                .build();

        memberRepository.saveAll(Arrays.asList(member1, member2));
        interestRepository.saveAll(Arrays.asList(interest1, interest2, interest3, interest4));
        memberInterestRepository.saveAll(Arrays.asList(memberInterest1, memberInterest2, memberInterest3, memberInterest4, memberInterest5));
        groupRepository.saveAll(Arrays.asList(studyGroup1, studyGroup2, studyGroup3, studyGroup4, studyGroup5, studyGroup6));
        groupMemberRepository.saveAll(Arrays.asList(groupMember1, groupMember2, groupMember3, groupMember4, groupMember5, groupMember6, groupMember7, groupMember8));
    }

}