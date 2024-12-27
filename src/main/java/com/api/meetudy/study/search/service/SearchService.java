package com.api.meetudy.study.search.service;

import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.enums.GroupMemberStatus;
import com.api.meetudy.study.group.enums.Location;
import com.api.meetudy.study.group.enums.StudyCategory;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.GroupMemberRepository;
import com.api.meetudy.study.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;

    @Transactional(readOnly = true)
    public List<StudyGroupDto> searchStudyGroups(String searchKeyword) {
        Specification<StudyGroup> spec = Specification.where(StudyGroupSpecification.isRecruitingSpecification());

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            spec = spec.and(StudyGroupSpecification.searchKeywordSpecification(searchKeyword));
        }

        List<StudyGroup> searchResults = groupRepository.findAll(spec);
        return studyGroupMapper.toStudyGroupDtoList(searchResults);
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> filterStudyGroups(String maxParticipantsCondition, Boolean isOnline,
                                                 StudyCategory category, Location location) {
        Specification<StudyGroup> spec = Specification.where(StudyGroupSpecification.isRecruitingSpecification());

        spec = spec.and(StudyGroupSpecification.maxParticipantsSpecification(maxParticipantsCondition));
        spec = spec.and(StudyGroupSpecification.isOnlineSpecification(isOnline));
        spec = spec.and(StudyGroupSpecification.categorySpecification(category));
        spec = spec.and(StudyGroupSpecification.locationSpecification(location));

        List<StudyGroup> filteredGroups = groupRepository.findAll(spec);
        return studyGroupMapper.toStudyGroupDtoList(filteredGroups);
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> sortStudyGroups(List<Long> groupIds, String sortBy) {
        List<StudyGroup> groups = groupRepository.findAllById(groupIds);

        switch (sortBy) {
            case "LATEST":
                groups.sort(Comparator.comparing(StudyGroup::getCreatedAt).reversed());
                break;
            case "OLDEST":
                groups.sort(Comparator.comparing(StudyGroup::getCreatedAt));
                break;
            case "POPULAR":
                groups.sort(Comparator.comparingInt(this::getJoinRequestCount).reversed());
                break;
            case "CLOSING_SOON":
                groups.sort(Comparator.comparingInt(this::getParticipantsDiff));
                break;
            default:
                break;
        }

        return studyGroupMapper.toStudyGroupDtoList(groups);
    }

    private int getJoinRequestCount(StudyGroup studyGroup) {
        return (int) groupMemberRepository.countByStudyGroupAndStatus(studyGroup, GroupMemberStatus.PENDING);
    }

    private int getParticipantsDiff(StudyGroup studyGroup) {
        long currentMembersCount = groupMemberRepository.countByStudyGroupAndStatus(studyGroup, GroupMemberStatus.ACCEPTED);
        return Math.abs((int) (studyGroup.getMaxParticipants() - currentMembersCount));
    }

}