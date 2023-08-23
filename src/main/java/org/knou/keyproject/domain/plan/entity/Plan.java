package org.knou.keyproject.domain.plan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.entity.DateType;
import org.knou.keyproject.domain.board.entity.Board;
import org.knou.keyproject.domain.bookchapter.entity.BookChapter;
import org.knou.keyproject.domain.chatgpt.entity.ChatGptResponseLine;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.global.audit.BaseTimeEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 2023.7.19(수) 18h20 -> 2023.7.20(목) 17h v1
@Slf4j
@Builder
@Getter
//@Setter // 2023.7.24(월) 22h 계산기 구현하며 고민하다가 추가 + 2023.8.6(일) 0h45 BeanUtils 사용하려고 추가
@NoArgsConstructor/*(access = AccessLevel.PROTECTED)*/
@AllArgsConstructor
@Entity
public class Plan extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

//    @JsonManagedReference
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "plan"/*, cascade = CascadeType.ALL*/)
    private List<BookChapter> bookChapterList = new ArrayList<>();

    // 2023.8.23(수) 17h10
    @OneToMany(mappedBy = "plan")
    private List<ChatGptResponseLine> chatGptResponseLineList = new ArrayList<>();

//    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
//    private List<Scrap> scrapList = new ArrayList<>();

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
    @Column(nullable = true)
    private DeadlineType deadlineType; // D(Date) = 특정 날짜, P(Period) = 기간

    private LocalDate deadlineDate;

    private String deadlinePeriod;
    // 2023.8.2(수) 2H15 계산기 UI 바꿀 경우 추가될 멤버변수
    private Integer deadlinePeriodNum;
    private String deadlinePeriodUnit;

    private Integer quantityPerDayPredicted;

    // 2023.8.7(월) 8h30 추가
    @Column(columnDefinition = "LONGTEXT")
    private String chatGptResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_PLAN_ID"/*, referencedColumnName = "PARENT_PLAN_ID"*/)
    private Plan parentPlan;

    @OneToMany(mappedBy = "parentPlan", fetch = FetchType.LAZY)
    private List<Plan> modifiedPlans = new ArrayList<>();

    // 2023.8.6(일) 3h50 resumedPlan을 db에 저장하며 추가
    public void addModifiedPlan(Plan modifiedPlan) {
        this.modifiedPlans.add(modifiedPlan);
        if (modifiedPlan.getParentPlan() != this) {
            modifiedPlan.setParentPlan(this);
        }
    }

    public void setModifiedPlans(List<Plan> modifiedPlans) {
        this.modifiedPlans = modifiedPlans;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "'ACTIVE'")
    private PlanStatus status; // A(Active) = 수행 중, C(Complete) = 완료, P(Pause) = 일시 정지, G(Give up) = 중도 포기, N(No) = 삭제

    // 2023.7.28(금) 22h55 '일정 상세 조회' 화면 구현 시 추가해봄
    private LocalDate lastStatusChangedAt;

    // 2023.7.24(월) 1h45 활동 계획 계산 결과 관련 추가
    private Integer totalDurationDays;
    private Integer totalNumOfActions;
    private Integer quantityPerDay;
    private Double frequencyFactor;

    // 2023.8.6(일) 0h40
    private Boolean isChild;

    // 2023.8.21(월) 14h45
    private Boolean isBook;
    private String isbn13;
    private String tableOfContents;

    // 2023.7.26(수) 14h
    // 누적 수행/기록량, 횟수

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
    public void setMember(Member member) {
        this.member = member;
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

    // 2023.8.2(수) 2h25 측정 어려운 일의 actionDates를 구하기 위해 추가
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    // 2023.8.5(토) 15h40 특정 plan의 resume/pause/giveUp 처리하기 위해 추가
    public void setLastStatusChangedAt(LocalDate lastStatusChangedAt) {
        this.lastStatusChangedAt = lastStatusChangedAt;
    }

    // 2023.8.5(토) 16h50 plan을 resume하며 '수정된 plan'으로 하나의 별도 레코드로 만들기 위해 추가
    public void setParentPlan(Plan parentPlan) {
        this.parentPlan = parentPlan;
    }

    // 2023.8.6(일) 1h5 plan resume 시 추가
    public void setIsChild(Boolean isChild) {
        this.isChild = isChild;
    }

    // 2023.7.25(화) 23h50
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    @Column
    List<ActionDate> actionDatesList = new ArrayList<>();

    // 2023.7.26(수) 2h35
    public List<ActionDate> getActionDatesList() {
        if (this.actionDatesList == null) {
            return new ArrayList<>();
        }

        return this.actionDatesList;
    }

    // 2023.7.26(수) 3h 빈도 정보 잘 들어가고 start~dead 순회 잘 하는데, actionList가 비는 것에 대해 디버깅하다가 참고로 써봄
//    public void setActionDays(List<ActionDate> actionDays) {
//        this.actionDays = actionDays;
//    }

    // 2023.7.29(토) 4h30 추가
    public void setActionDatesList(List<ActionDate> actionDatesList) {
        this.actionDatesList = actionDatesList;
    }

    // 2023.7.29(토) 2h25 아래 작업을 Calculator로 옮김
//    public void setActionDatesList() {
//        List<ActionDate> actionDays = new ArrayList<>();
//        this.actionDatesList = actionDays;
//    }

    // 2023.8.4(금) 23h PlanService에서 처리했었으나, service 클래스/메소드는 트랜잭션과 도메인 간 순서만 보장 + 비즈니스 로직 처리는 도메인이 담당
    // 그런데 그러려면 엔티티에서 repository를 사용해야 하는데, 이상한 것 같다 + 엔티티의 성질을 바꾸는(x) 엔티티의 성질을 가지고 통계를 내는(O) 기능이므로, 여기는 아닌 것 같네..
//    public Integer getAccumulatedRealActionQuantity(Long planId) {
//
//    }

    // 2023.8.5(토) 18h35
    public void pauseActionDates() {
        List<ActionDate> actionDatesToPause = this.actionDatesList;
        for (ActionDate actionDate : actionDatesToPause) {
            LocalDate thisDate = LocalDate.parse(actionDate.getDateFormat(), DateTimeFormatter.ISO_DATE);
            if (thisDate.equals(LocalDate.now()) || thisDate.isAfter(LocalDate.now())) {
                actionDate.setDateType(DateType.PAUSE);
            }
        }

        this.actionDatesList = actionDatesToPause;
    }

    // 2023.8.5(토) 18h45
    public void giveUpActionDates() {
        List<ActionDate> actionDatesToGiveUp = this.actionDatesList;
        for (ActionDate actionDate : actionDatesToGiveUp) {
            LocalDate thisDate = LocalDate.parse(actionDate.getDateFormat(), DateTimeFormatter.ISO_DATE);
            if (thisDate.equals(LocalDate.now()) || thisDate.isAfter(LocalDate.now())) {
                actionDate.setDateType(DateType.GIVEUP);
            }
        }

        this.actionDatesList = actionDatesToGiveUp;
    }
}
