package org.knou.keyproject.domain.scrap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.global.audit.BaseTimeEntity;

// 2023.7.22(토) 0h55
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Scrap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAN_ID")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
}
