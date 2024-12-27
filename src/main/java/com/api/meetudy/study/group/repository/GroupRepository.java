package com.api.meetudy.study.group.repository;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<StudyGroup, Long>, JpaSpecificationExecutor<StudyGroup> {

    List<StudyGroup> findByLeader(Member member);

}