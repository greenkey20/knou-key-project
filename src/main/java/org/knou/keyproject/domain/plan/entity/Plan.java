package org.knou.keyproject.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.knou.keyproject.domain.action.entity.Action;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.scrap.entity.Scrap;
import org.knou.keyproject.global.audit.BaseTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 2023.7.19(수) 18h20 -> 2023.7.20(목) 17h v1
@Builder
@Getter
//@Setter // 2023.7.24(월) 22h 계산기 구현하며 고민하다가 추가
@AllArgsConstructor
@NoArgsConstructor/*(access = AccessLevel.PROTECTED)*/
@Entity
public class Plan extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "INTEGER", name = "MEMBER_ID")
    private Member planner;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Action> actionList = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Scrap> scrapList = new ArrayList<>();

    @Column(nullable = false)
    private Boolean isMeasurable; // true = 측정 가능한 일 vs false = 측정 어려운 일

    @Column(nullable = false)
    private String object; // 수행 목표 대상

    // 측정 가능한 일인 경우(2023.7.21(금) 현재 ERD 상에는 별도 테이블로 작성되어있음)
    private Integer totalQuantity;
    private String unit;

    @Column(nullable = false)
    private Boolean hasStartDate;

    private LocalDate startDate; // 시작일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FrequencyType frequencyType; // 수행 빈도 종류, D(Date) = 특정 요일 선택, E(Every) = x일마다 1회, T(Times) = 주/월 x회

    @Column(nullable = false)
    private String frequencyDetail;

    @Column(nullable = false)
    private Boolean hasDeadline;

    @Enumerated(EnumType.STRING)
    private DeadlineType deadlineType; // D(Date) = 특정 날짜, P(Period) = 기간

    private LocalDate deadlineDate;
    private String deadlinePeriod;

    private Integer quantityPerDayPredicted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_PLAN_ID"/*, referencedColumnName = "PARENT_PLAN_ID"*/)
    private Plan parentPlan;

    @OneToMany(mappedBy = "parentPlan", fetch = FetchType.LAZY)
    private List<Plan> modifiedPlans = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "'ACTIVE'")
    private PlanStatus status; // A(Active) = 수행 중, C(Complete) = 완료, P(Pause) = 일시 정지, G(Give up) = 중도 포기, N(No) = 삭제

    // 2023.7.24(월) 1h45 활동 계획 계산 결과 관련 추가
    private Integer totalDurationDays;
    private Integer totalNumOfActions;
    private Integer quantityPerDay;
    private Double frequencyFactor;

    // 2023.7.23(일) 21h40
    /*
    public void setIsMeasurable(Boolean isMeasurable) {
        this.isMeasurable = isMeasurable;
    }

    // 2023.7.23(일) 21h50
    public void setFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }

    public void setHasDeadline(Boolean hasDeadline) {
        this.hasDeadline = hasDeadline;
    }

    public void setDeadlineType(DeadlineType deadlineType) {
        this.deadlineType = deadlineType;
    }
    */

    // 2023.7.24(월) 17h45
    public void setPlanner(Member planner) {
        this.planner = planner;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setStatus(PlanStatus status) {
        this.status = status;
    }

    // controller 테스트 시 추가
    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    // 2023.7.24(월) 22h 계산기 구현하며 고민하다 추가
    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public void setFrequencyDetail(String frequencyDetail) {
        this.frequencyDetail = frequencyDetail;
    }

    public void setTotalDurationDays(Integer totalDurationDays) {
        this.totalDurationDays = totalDurationDays;
    }

    public void setTotalNumOfActions(Integer totalNumOfActions) {
        this.totalNumOfActions = totalNumOfActions;
    }

    public void setQuantityPerDay(Integer quantityPerDay) {
        this.quantityPerDay = quantityPerDay;
    }

    public void setFrequencyFactor(Double frequencyFactor) {
        this.frequencyFactor = frequencyFactor;
    }
}
