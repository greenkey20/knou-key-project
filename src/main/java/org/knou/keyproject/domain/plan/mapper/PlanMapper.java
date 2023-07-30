package org.knou.keyproject.domain.plan.mapper;

import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.plan.dto.*;
import org.knou.keyproject.domain.plan.entity.DeadlineType;
import org.knou.keyproject.domain.plan.entity.FrequencyType;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// 2023.7.28(금) 1h25
@Mapper(componentModel = "spring")
public interface PlanMapper {

    // view로부터 입력받은 정보만 Plan 객체에 맞춰서 (변환하고) 채움
    default Plan toEntity(PlanPostRequestDto planPostRequestDto) {
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

        if (planPostRequestDto.getIsMeasurableNum() == 1) {
            plan.isMeasurable(true);
        } else {
            plan.isMeasurable(false);
        }

        plan.object(planPostRequestDto.getObject());
        plan.totalQuantity(planPostRequestDto.getTotalQuantity());
        plan.unit(planPostRequestDto.getUnit());

        if (planPostRequestDto.getHasStartDate() == 1) {
            plan.hasStartDate(true);
        } else {
            plan.hasStartDate(false);
        }

        plan.startDate(planPostRequestDto.getStartDate());

        switch (planPostRequestDto.getFrequencyTypeNum()) {
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

        plan.frequencyDetail(planPostRequestDto.getFrequencyDetail());

        if (planPostRequestDto.getHasDeadline() == 1) {
            plan.hasDeadline(true);

            if (planPostRequestDto.getDeadlineTypeNum() == 1) {
                plan.deadlineType(DeadlineType.DATE);
                plan.deadlineDate(planPostRequestDto.getDeadlineDate());
            } else {
                plan.deadlineType(DeadlineType.PERIOD);
                plan.deadlinePeriod(planPostRequestDto.getDeadlinePeriod());

            }
        } else {
            plan.hasDeadline(false);
            plan.quantityPerDayPredicted(planPostRequestDto.getQuantityPerDayPredicted());
        }

        plan.status(PlanStatus.RESULT);

        return plan.build();
    }

    Plan toEntity(MyPlanPostRequestDto myPlanPostRequestDto);

    default NewPlanResponseDto toNewPlanResponseDto(Plan entity) {
        if (entity == null) {
            return null;
        }

        NewPlanResponseDto.NewPlanResponseDtoBuilder newPlanResponseDto = NewPlanResponseDto.builder();

        newPlanResponseDto.planId(entity.getPlanId());
        newPlanResponseDto.object(entity.getObject());
        newPlanResponseDto.totalQuantity(entity.getTotalQuantity());
        newPlanResponseDto.unit(entity.getUnit());
        newPlanResponseDto.hasStartDate(entity.getHasStartDate());
        newPlanResponseDto.startDate(entity.getStartDate());
        newPlanResponseDto.startYear(entity.getStartDate().getYear());
        newPlanResponseDto.startMonth(entity.getStartDate().getMonthValue());

        if (entity.getHasDeadline()) {
            newPlanResponseDto.deadlineDate(entity.getDeadlineDate());
            newPlanResponseDto.deadlineYear(entity.getDeadlineDate().getYear());
            newPlanResponseDto.deadlineMonth(entity.getDeadlineDate().getMonthValue());
        } else {
            ActionDate lastActionDate = entity.getActionDatesList().get(entity.getActionDatesList().size() - 1);
            LocalDate deadlineDate = LocalDate.of(Integer.valueOf(lastActionDate.getNumOfYear()), lastActionDate.getNumOfMonth(), Integer.parseInt(lastActionDate.getNumOfDate()));
            newPlanResponseDto.deadlineDate(deadlineDate);
            newPlanResponseDto.deadlineYear(deadlineDate.getYear());
            newPlanResponseDto.deadlineMonth(deadlineDate.getMonthValue());
        }

        newPlanResponseDto.hasDeadline(entity.getHasDeadline());
        newPlanResponseDto.totalDurationDays(entity.getTotalDurationDays());
        newPlanResponseDto.frequencyDetail(entity.getFrequencyDetail());
        newPlanResponseDto.totalNumOfActions(entity.getTotalNumOfActions());
        newPlanResponseDto.quantityPerDay(entity.getQuantityPerDay());

        // 2023.7.28(금) 3h25 이 부분 매핑 자동으로 안 되어서 null pointer exception 발생 -> ActionDateMapperImpl 참고해서 수기로 작성
        List<ActionDateResponseDto> actionDates = new ArrayList<>();

        for (int i = 0; i < entity.getActionDatesList().size(); i++) {
            ActionDate thisActionDate = entity.getActionDatesList().get(i);

            if (thisActionDate == null) {
//                System.out.println("----------PlanMapper 여기에서 이미 null이에요-----------");
                return null;
            }

//            System.out.println("----------PlanMapper 여기에서 null은 아니에요-----------");

            ActionDateResponseDto.ActionDateResponseDtoBuilder actionDateResponseDto = ActionDateResponseDto.builder();

            // 2023.7.31(월) 0h5 나의 생각 = 이렇게 수기로 다 작성하는 게 좋은/맞는 방법 같지 않은데.. MapStruct 이용할 방법이 있을텐데!
            actionDateResponseDto.actionDateId(thisActionDate.getActionDateId());
            actionDateResponseDto.planId(thisActionDate.getPlan().getPlanId());

            actionDateResponseDto.numOfYear(thisActionDate.getNumOfYear());
            if (thisActionDate.getNumOfMonth() != null) {
                actionDateResponseDto.numOfMonth(String.valueOf(thisActionDate.getNumOfMonth()));
            }
            actionDateResponseDto.numOfDate(thisActionDate.getNumOfDate());
            actionDateResponseDto.numOfDay(thisActionDate.getNumOfDay());
            actionDateResponseDto.dateFormat(thisActionDate.getDateFormat());

            actionDateResponseDto.dateType(thisActionDate.getDateType());
            actionDateResponseDto.schedule(thisActionDate.getSchedule());

            actionDateResponseDto.memo(thisActionDate.getMemo());
            actionDateResponseDto.planActionQuantity(thisActionDate.getPlanActionQuantity());
            actionDateResponseDto.isDone(thisActionDate.getIsDone());
            actionDateResponseDto.realActionQuantity(thisActionDate.getRealActionQuantity());
            actionDateResponseDto.timeTakeForRealAction(thisActionDate.getTimeTakenForRealAction());
            actionDateResponseDto.reviewScore(thisActionDate.getReviewScore());

            actionDates.add(actionDateResponseDto.build());
        }

        // 2023.7.28(금) 4h30 이 줄을 안 써서 1시간 이상 디버깅..
        newPlanResponseDto.actionDates(entity.getActionDatesList());

        return newPlanResponseDto.build();
    }

    MyPlanListResponseDto toMyPlanListResponseDto(Plan entity);

    MyPlanDetailResponseDto toMyPlanDetailResponseDto(Plan entity);

    // 2023.7.30(일) 21h5
    List<MyPlanDetailResponseDto> toMyPlanDetailResponseDtos(List<Plan> entities);
}
