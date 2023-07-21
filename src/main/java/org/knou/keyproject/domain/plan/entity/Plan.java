package org.knou.keyproject.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.knou.keyproject.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 2023.7.19(수) 18h20 -> 2023.7.20(목) 17h v1
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member planner;

    @Column(nullable = false)
    private Boolean isMeasurable; // true = 측정 가능한 일 vs false = 측정 어려운 일

    @Column(nullable = false)
    private String object; // 수행 목표 대상

    // 측정 가능한 일인 경우
    private Long totalQuantity;
    private String unit;

    private LocalDateTime startDate; // 시작일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FrequencyType frequencyType; // 수행 빈도 종류, D(Date) = 특정 요일 선택, E(Every) = x일마다 1회, T(Times) = 주/월 x회

    public enum FrequencyType {
        DATE(1),
        EVERY(2),
        TIMES(3);

        @Getter
        private int num;

        FrequencyType(int num) {
            this.num = num;
        }
    }

    @Column(nullable = false)
    private String frequencyDetail;

    @Column(nullable = false)
    private Boolean hasDeadline;

    @Enumerated(EnumType.STRING)
    private DeadlineType deadlineType; // D(Date) = 특정 날짜, P(Period) = 기간

    public enum DeadlineType {
        DATE(1),
        PERIOD(2);

        @Getter
        private int num;

        DeadlineType(int num) {
            this.num = num;
        }
    }

    private LocalDateTime deadlineDate;
    private String deadlinePeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_ID", referencedColumnName = "PARENT_PLAN_ID")
    private Plan parentPlan;

    @OneToMany(mappedBy = "parentPlan", fetch = FetchType.LAZY)
    private List<Plan> modifiedPlans = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "ACTIVE")
    private PlanStatus status; // A(Active) = 수행 중, C(Complete) = 완료, P(Pause) = 일시 정지, G(Give up) = 중도 포기, N(No) = 삭제

    public enum PlanStatus {
        ACTIVE(1),
        COMPLETE(2),
        PAUSE(3),
        GIVEUP(4),
        DELETE(5);

        @Getter
        private int num;

        PlanStatus(int num) {
            this.num = num;
        }
    }
}
