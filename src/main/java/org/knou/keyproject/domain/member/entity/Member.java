package org.knou.keyproject.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.knou.keyproject.domain.plan.entity.Plan;

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

    @OneToMany(mappedBy = "planner")
    private List<Plan> plans = new ArrayList<>();

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

    public enum Gender {
        MALE(1),
        FEMALE(2),
        OTHERS(3);

        @Getter
        private int num;

        Gender(int num) {
            this.num = num;
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "ACTIVE")
    private MemberStatus status;

    public enum MemberStatus {
        ACTIVE(1),
        QUIT(2),
        BANNED(3);

        @Getter
        private int num;

        MemberStatus(int num) {
            this.num = num;
        }
    }

    // 코드스테이츠 컨텐츠 방식
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    private String profileImageUrl;

    @Column(nullable = false)
    private LocalDateTime lastLoginAt;
}
