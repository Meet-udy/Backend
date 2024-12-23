package com.api.meetudy.study.recommendation.repository;

import com.api.meetudy.study.group.enums.StudyCategory;
import com.api.meetudy.study.recommendation.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    Interest findByStudyCategory(StudyCategory studyCategory);

}