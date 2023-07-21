package org.knou.keyproject.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.scrap.entity.Scrap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 2023.7.19(수) 18h25 -> 2023.7.20(목) 17h30 v1
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL)
    private List<Plan> planList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Scrap> scrapList = new ArrayList<>();

    @Column(nullable = false)
    private String memberPlatform;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    private Boolean isEmailVerified;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private Integer age;
    private Integer yearOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "ACTIVE")
    private MemberStatus status;

    // 2023.7.21(금) 18h15 수정 = 도서관 references 책
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String profileImageUrl;

    @Column(nullable = false)
    private LocalDateTime lastLoginAt;
}
