package com.api.meetudy.study.recommendation.repository;

import com.api.meetudy.study.recommendation.entity.MemberInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {

}