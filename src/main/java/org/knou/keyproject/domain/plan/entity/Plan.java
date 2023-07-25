package org.knou.keyproject.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.knou.keyproject.domain.action.entity.Action;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.scrap.entity.Scrap;
import org.knou.keyproject.global.audit.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// 2023.7.19(수) 18h20 -> 2023.7.20(목) 17h v1
@Slf4j
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
    private Member member;

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

    // 2023.7.25(화) 23h50
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    @Column
    List<DateData> actionDays = new ArrayList<>();

    // 2023.7.26(수) 2h35
    public List<DateData> getActionDays() {
        if (this.actionDays == null) {
            return new ArrayList<>();
        }

        return this.actionDays;
    }

    // 2023.7.26(수) 3h 빈도 정보 잘 들어가고 start~dead 순회 잘 하는데, actionList가 비는 것에 대해 디버깅하다가 참고로 써봄
//    public void setActionDays(List<DateData> actionDays) {
//        this.actionDays = actionDays;
//    }

    public void setActionDaysList() {
        List<DateData> actionDays = new ArrayList<>();

        switch (this.frequencyType) {
            case DATE: // frequencyType이 DATE일 때 활동일 목록을 구함
                actionDays = getActionDaysWithFrequencyTypeDATE(actionDays);
                break;
            case EVERY: // EVERY-TIMES 계산 원리는 비슷
                actionDays = getActionDaysWithFrequencyTypeEVERY(actionDays);
                break;
            case TIMES:
                actionDays = getActionDaysWithFrequencyTypeTIMES(actionDays);
                break;
        }

        this.actionDays = actionDays;
    }

    /**
     * 주/월 x회마다 활동하는 경우의 활동일 리스트 만드는 메서드
     * e.g. 주 3회, 월 10회 등
     *
     * @return
     */
    private List<DateData> getActionDaysWithFrequencyTypeTIMES(List<DateData> actionDays) {
        // timeUnit 관련 데이터 가공
        char timeUnit = this.frequencyDetail.charAt(0);

        int interval = 0;

        switch (timeUnit) {
            case '주':
                interval = 7;
                break;
            case '월':
                interval = 30;
                break;
        }

        // 활동 횟수 관련 데이터 가공
        String obj = this.frequencyDetail.split(" ")[1];
        StringBuilder nums = new StringBuilder();

        for (int i = 0; i < obj.length(); i++) {
            char ch = obj.charAt(i);

            if (Character.isDigit(ch)) {
                nums.append(ch);
            }
        }

        int times = Integer.parseInt(nums.toString());

        // 빈도 조건에 맞는 활동일 찾기
        for (LocalDate date = this.startDate; date.isBefore(this.deadlineDate); date = date.plusDays(interval)) {
            actionDays.add(changeActionDateIntoActionDateData(date));

            for (int i = 1; i < times; i++) {
                int plusDay = interval / times;
                actionDays.add(changeActionDateIntoActionDateData(date.plusDays(plusDay)));
            }
        }
        return actionDays;
    }

    /**
     * 매 x일마다 활동하는 경우의 활동일 리스트 만드는 메서드
     * e.g. 2일마다 1회, 5일마다 2회, 10일마다 3회 등
     *
     * @return
     */
    private List<DateData> getActionDaysWithFrequencyTypeEVERY(List<DateData> actionDays) {
        int interval = Character.getNumericValue(frequencyDetail.split(" ")[0].charAt(0));
        int times = Character.getNumericValue(frequencyDetail.split(" ")[1].charAt(0));

        for (LocalDate date = this.startDate; date.isBefore(this.deadlineDate); date = date.plusDays(interval)) {
            actionDays.add(changeActionDateIntoActionDateData(date));

            for (int i = 1; i < times; i++) {
                int plusDay = interval / times;
                actionDays.add(changeActionDateIntoActionDateData(date.plusDays(plusDay)));
            }
        }
        return actionDays;
    }

    private List<DateData> getActionDaysWithFrequencyTypeDATE(List<DateData> actionDays) {
        // 활동하는 요일들의 정수 값을 리스트로 받음
        String daysStr = this.frequencyDetail.substring(0, this.frequencyDetail.length() - 4);
        List<Integer> daysList = getActionWeekdays(daysStr);
        log.info("getActionDaysWithFrequencyTypeDATE에서 daysList = " + daysList);

        for (LocalDate date = this.startDate; date.isBefore(this.deadlineDate); date = date.plusDays(1)) {
            // 순회 중인 날이 해당 요일이면 활동일 리스트에 담음
            int dayOfDate = date.getDayOfWeek().getValue();
            log.info("순회 중인 date의 요일 번호 = " + dayOfDate);

            if (daysList.contains(dayOfDate)) {
                actionDays.add(changeActionDateIntoActionDateData(date));
            }
        }

        log.info("순회 마치고 actionDays 리스트 = " + getActionDays());
        return actionDays;
    }

    private DateData changeActionDateIntoActionDateData(LocalDate date) {
        return new DateData(
                String.valueOf(date.getYear()),
                date.getMonthValue(),
                String.valueOf(date.getDayOfMonth()),
                date.getDayOfWeek().getValue(),
                "action"
        );
    }

    private List<Integer> getActionWeekdays(String daysStr) {
        List<Integer> daysList = new ArrayList<>();
        int day = -1;

        for (int i = 0; i < daysStr.length(); i++) {
            char ch = daysStr.charAt(i);

            switch (ch) {
                case '월':
                    day = 1;
                    daysList.add(day);
                    break;
                case '화':
                    day = 2;
                    daysList.add(day);
                    break;
                case '수':
                    day = 3;
                    daysList.add(day);
                    break;
                case '목':
                    day = 4;
                    daysList.add(day);
                    break;
                case '금':
                    day = 5;
                    daysList.add(day);
                    break;
                case '토':
                    day = 6;
                    daysList.add(day);
                    break;
                default:
            }
        }

        return daysList;
    }
}
