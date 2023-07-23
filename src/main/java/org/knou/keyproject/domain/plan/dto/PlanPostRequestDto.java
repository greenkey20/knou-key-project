package org.knou.keyproject.domain.plan.dto;

import lombok.*;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.domain.plan.entity.DeadlineType;
import org.knou.keyproject.domain.plan.entity.FrequencyType;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.global.exception.BusinessLogicException;
import org.knou.keyproject.global.exception.ExceptionCode;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 2023.7.23(일) 20h45
@Getter
@Setter
//@RequiredArgsConstructor
@NoArgsConstructor
public class PlanPostRequestDto {
//    private MemberRepository memberRepository;
//
//    private Long plannerId;
    private int isMeasurableNum;
    private String object;
    private Long totalQuantity;
    private String unit;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // yyyy-MM-dd'T'00:mm:ss
    private LocalDate startDate;

    private int frequencyTypeNum;
    private String frequencyDetail;

    private int hasDeadline;
    private int deadlineTypeNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadlineDate;
    private String deadlinePeriod;
    private Long quantityPerDayPredicted;

//    public PlanPostRequestDto(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    @Builder
    public PlanPostRequestDto(int isMeasurableNum, String object, Long totalQuantity, String unit,
                              LocalDate startDate, int frequencyTypeNum, String frequencyDetail,
                              int hasDeadline, int deadlineTypeNum, LocalDate deadlineDate, String deadlinePeriod, Long quantityPerDayPredicted) {
        this.isMeasurableNum = isMeasurableNum;
        this.object = object;
        this.totalQuantity = totalQuantity;
        this.unit = unit;
        this.startDate = startDate;
        this.frequencyTypeNum = frequencyTypeNum;
        this.frequencyDetail = frequencyDetail;
        this.hasDeadline = hasDeadline;
        this.deadlineTypeNum = deadlineTypeNum;
        this.deadlineDate = deadlineDate;
        this.deadlinePeriod = deadlinePeriod;
        this.quantityPerDayPredicted = quantityPerDayPredicted;
    }

    public Plan toEntity() {
//        Plan plan0 = new Plan();
        // 2023.7.23(일) 22h55 코드스테이츠 컨텐츠 보다가 mapper 코드 보니 아래와 같이 setter 없이 값 세팅 가능..
        Plan.PlanBuilder plan = Plan.builder();

//        Member planner = new Member();
//
//        if (plannerId != null) {
//            planner = memberRepository.findById(plannerId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//        }
//
//        plan.planner(planner);

        if (isMeasurableNum == 1) {
            plan.isMeasurable(true);
        } else {
            plan.isMeasurable(false);
        }

        plan.object(object);
        plan.totalQuantity(totalQuantity);
        plan.unit(unit);
        plan.startDate(startDate);

        switch (frequencyTypeNum) {
            case 1:
                plan.frequencyType(FrequencyType.DATE);
                break;
            case 2:
                plan.frequencyType(FrequencyType.EVERY);
                break;
            case 3:
                plan.frequencyType(FrequencyType.TIMES);
                break;
        }

        plan.frequencyDetail(frequencyDetail);

        if (hasDeadline == 1) {
            plan.hasDeadline(true);

            if (deadlineTypeNum == 1) {
                plan.deadlineType(DeadlineType.DATE);
                plan.deadlineDate(deadlineDate);
            } else {
                plan.deadlineType(DeadlineType.PERIOD);
                plan.deadlinePeriod(deadlinePeriod);

            }
        } else {
            plan.hasDeadline(false);
            plan.quantityPerDayPredicted(quantityPerDayPredicted);
        }

        plan.status(PlanStatus.ACTIVE);

        return plan.build();
    }

    @Override
    public String toString() {
        return "PlanPostRequestDto{" +
                ", isMeasurableNum=" + isMeasurableNum +
                ", object='" + object + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", unit='" + unit + '\'' +
                ", startDate=" + startDate +
                ", frequencyTypeNum=" + frequencyTypeNum +
                ", frequencyDetail='" + frequencyDetail + '\'' +
                ", hasDeadline=" + hasDeadline +
                ", deadlineTypeNum=" + deadlineTypeNum +
                ", deadlineDate=" + deadlineDate +
                ", deadlinePeriod='" + deadlinePeriod + '\'' +
                ", quantityPerDayPredicted=" + quantityPerDayPredicted +
                '}';
    }
}
