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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 2023.7.23(일) 20h45
//@NoArgsConstructor
//@Builder
@Getter
@Setter
@RequiredArgsConstructor
public class PlanPostRequestDto {

    final MemberRepository memberRepository;
    private Long memberId;
    private Integer isMeasurableNum;
    private String object;
    private Integer totalQuantity;
    private String unit;
    private Integer hasStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // yyyy-MM-dd'T'00:mm:ss
    private LocalDate startDate;

    private Integer frequencyTypeNum;
    private String frequencyDetail;

    private Integer hasDeadline;
    private Integer deadlineTypeNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadlineDate;
    private String deadlinePeriod;
    private Integer quantityPerDayPredicted;

//    public PlanPostRequestDto(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Builder
//    public PlanPostRequestDto(Long memberId, Integer isMeasurableNum, String object, Integereger totalQuantity, String unit,
//                              Integer hasStartDate, LocalDate startDate, Integer frequencyTypeNum, String frequencyDetail,
//                              Integer hasDeadline, Integer deadlineTypeNum, LocalDate deadlineDate, String deadlinePeriod, Integereger quantityPerDayPredicted) {
//        this.memberId = memberId;
//        this.isMeasurableNum = isMeasurableNum;
//        this.object = object;
//        this.totalQuantity = totalQuantity;
//        this.unit = unit;
//        this.hasStartDate = hasStartDate;
//        this.startDate = startDate;
//        this.frequencyTypeNum = frequencyTypeNum;
//        this.frequencyDetail = frequencyDetail;
//        this.hasDeadline = hasDeadline;
//        this.deadlineTypeNum = deadlineTypeNum;
//        this.deadlineDate = deadlineDate;
//        this.deadlinePeriod = deadlinePeriod;
//        this.quantityPerDayPredicted = quantityPerDayPredicted;
//    }

    // view로부터 입력받은 정보만 Plan 객체에 맞춰서 (변환하고) 채움
    public Plan toEntity() {
//        Plan plan0 = new Plan();
        // 2023.7.23(일) 22h55 코드스테이츠 컨텐츠 보다가 mapper 코드 보니 아래와 같이 setter 없이 값 세팅 가능..
        Plan.PlanBuilder plan = Plan.builder();

//        Member findMember = null;
//
//        if (memberId != null) {
//            findMember = memberRepository.findById(memberId).orElse(null);
//        }
//
//        plan.member(findMember);

        if (isMeasurableNum == 1) {
            plan.isMeasurable(true);
        } else {
            plan.isMeasurable(false);
        }

        plan.object(object);
        plan.totalQuantity(totalQuantity);
        plan.unit(unit);

        if (hasStartDate == 1) {
            plan.hasStartDate(true);
        } else {
            plan.hasStartDate(false);
        }

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

        plan.status(PlanStatus.RESULT);

        return plan.build();
    }

    @Override
    public String toString() {
        return "PlanPostRequestDto{" +
                "isMeasurableNum=" + isMeasurableNum +
                ", object='" + object + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", unit='" + unit + '\'' +
                ", hasStartDate=" + hasStartDate +
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
