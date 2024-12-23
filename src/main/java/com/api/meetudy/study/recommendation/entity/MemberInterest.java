package com.api.meetudy.study.recommendation.entity;

import com.api.meetudy.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@ToString
@Table(name  = "member_interest")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id", nullable = false)
    private Interest interest;

    public MemberInterest(Member member, Interest interest) {
        this.member = member;
        this.interest = interest;
    }

}