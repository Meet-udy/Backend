package com.api.meetudy.member.repository;

import com.api.meetudy.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Member> findByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByUsernameAndEmail(String username, String email);

}