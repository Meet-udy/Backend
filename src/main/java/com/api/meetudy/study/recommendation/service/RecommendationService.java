package com.api.meetudy.study.recommendation.service;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.StudyGroupMemberRepository;
import com.api.meetudy.study.group.repository.StudyGroupRepository;
import com.api.meetudy.study.recommendation.entity.Interest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final StudyGroupRepository groupRepository;
    private final StudyGroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;

    public List<StudyGroupDto> recommendStudyGroups(Member member) {
        List<StudyGroupDto> recommendedGroups = new ArrayList<>();
        List<StudyGroup> allGroups = groupRepository.findAll();
        List<StudyGroup> excludedGroups = getMemberRelatedGroups(member);

        for (StudyGroup group : allGroups) {
            if (excludedGroups.contains(group)) {
                continue;
            }

            double score = calculateRecommendationScore(group, member, excludedGroups);
            if (score > 0) {
                StudyGroupDto groupDto = studyGroupMapper.toStudyGroupDto(group);
                groupDto.setScore(score);
                recommendedGroups.add(groupDto);
            }
        }

        return recommendedGroups.stream()
                .sorted(Comparator.comparingDouble(StudyGroupDto::getScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<StudyGroup> getMemberRelatedGroups(Member member) {
        List<StudyGroupMember> studyGroupMembers = groupMemberRepository.findByMember(member);
        List<StudyGroup> memberJoinedGroups = studyGroupMembers.stream()
                .map(StudyGroupMember::getStudyGroup)
                .collect(Collectors.toList());

        List<StudyGroup> leaderGroups = groupRepository.findByLeader(member);

        Set<StudyGroup> allRelatedGroups = new HashSet<>(memberJoinedGroups);
        allRelatedGroups.addAll(leaderGroups);

        return new ArrayList<>(allRelatedGroups);
    }

    private double calculateRecommendationScore(StudyGroup group, Member member, List<StudyGroup> memberRelatedGroups) {
        double score = 0.0;

        List<Interest> memberInterests = member.getInterests();
        for (Interest interest : memberInterests) {
            if (group.getCategory() == interest.getStudyCategory()) {
                score += 0.5;
            }
        }

        if (group.getLocation() == member.getLocation()) {
            score += 0.3;
        }

        if (Boolean.TRUE.equals(group.getIsOnline()) && Boolean.TRUE.equals(member.getIsOnline())) {
            score += 0.2;
        }

        if (memberRelatedGroups != null) {
            for (StudyGroup relatedGroup : memberRelatedGroups) {
                if (group.getCategory() == relatedGroup.getCategory()) {
                    score += 0.5;
                }

                if (group.getLocation() == relatedGroup.getLocation()) {
                    score += 0.3;
                }

                if (Boolean.TRUE.equals(group.getIsOnline()) && Boolean.TRUE.equals(relatedGroup.getIsOnline())) {
                    score += 0.2;
                }
            }
        }

        return score;
    }

}