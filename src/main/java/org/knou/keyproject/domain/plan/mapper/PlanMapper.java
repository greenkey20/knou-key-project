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
            plan.chatGptResponse(planPostRequestDto.getChatGptResponse());
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

        // 2023.8.7(월) 6h40 해당 UI 전체 적용
//        if (planPostRequestDto.getIsMeasurableNum() == 1) { // 측정 가능한 일일 경우 2023.8.2(화) 1h25 현재 UI = 사용자가 String으로 직접 입력
//            plan.frequencyDetail(planPostRequestDto.getFrequencyDetail());
//        } else { // 측정 어려운 일일 경우(ChatGPT 계산기로부터 정보 넘어오는 경우) 2023.8.2(화) 1h25 현재 UI = 사용자가 여러 칸 선택
        String frequencyDetail = "";
        switch (planPostRequestDto.getFrequencyTypeNum()) {
            case 1:
                for (String str : planPostRequestDto.getFrequencyDetailDate()) {
                    frequencyDetail += str;
                }
                break;
            case 2:
                frequencyDetail = planPostRequestDto.getFrequencyDetailEveryInterval() + "일마다 " + planPostRequestDto.getFrequencyDetailEveryTimes() + "회";
                break;
            case 3:
                frequencyDetail = planPostRequestDto.getFrequencyDetailTimesInterval() + " " + planPostRequestDto.getFrequencyDetailTimesTimes() + "회";
                break;
        }
        plan.frequencyDetail(frequencyDetail);
//        }

        if (planPostRequestDto.getHasDeadline() == 1) {
            plan.hasDeadline(true);

            if (planPostRequestDto.getDeadlineTypeNum() == 1) {
                plan.deadlineType(DeadlineType.DATE);
                plan.deadlineDate(planPostRequestDto.getDeadlineDate());
            } else {
                plan.deadlineType(DeadlineType.PERIOD);

                // 2023.8.7(월) 6h55 ui 전체 적용
//                if (planPostRequestDto.getIsMeasurableNum() == 1) {
//                    plan.deadlinePeriod(planPostRequestDto.getDeadlinePeriod());
//                } else {
                String deadlinePeriod = planPostRequestDto.getDeadlinePeriodNum() + planPostRequestDto.getDeadlinePeriodUnit();
                plan.deadlinePeriod(deadlinePeriod);

                plan.deadlinePeriodNum(planPostRequestDto.getDeadlinePeriodNum());
                plan.deadlinePeriodUnit(planPostRequestDto.getDeadlinePeriodUnit());
//                }
            }
        } else {
            plan.hasDeadline(false);
            plan.quantityPerDayPredicted(planPostRequestDto.getQuantityPerDayPredicted());
        }

        plan.status(PlanStatus.RESULT);

        // 2023.8.23(수) 22h20 추가
        plan.isChild(false);

        // 2023.8.21(월) 14h55
        if (planPostRequestDto.getIsBook() != null) {
            plan.isbn13(planPostRequestDto.getIsbn13());
            plan.isBook(true);
        } else {
            plan.isBook(false);
        }

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

        if (entity.getIsMeasurable()) {
            newPlanResponseDto.unit(entity.getUnit());
        }

        newPlanResponseDto.hasStartDate(entity.getHasStartDate());
        newPlanResponseDto.startDate(entity.getStartDate());
        newPlanResponseDto.startYear(entity.getStartDate().getYear());
        newPlanResponseDto.startMonth(entity.getStartDate().getMonthValue());

        // 2023.8.2(수) 3h25 추가
        if (entity.getDeadlinePeriod() != null) {
            newPlanResponseDto.deadlinePeriod(entity.getDeadlinePeriod());
        }

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
            actionDateResponseDto.timeTakenForRealAction(thisActionDate.getTimeTakenForRealAction());
            actionDateResponseDto.reviewScore(thisActionDate.getReviewScore());

            actionDates.add(actionDateResponseDto.build());
        }

        // 2023.7.28(금) 4h30 이 줄을 안 써서 1시간 이상 디버깅..
        newPlanResponseDto.actionDates(entity.getActionDatesList());

        return newPlanResponseDto.build();
    }

    MyPlanListResponseDto toMyPlanListResponseDto(Plan entity);

    ///////// 구현 클래스에서 가져옴 ㅠㅠ /////////
    // 2023.7.31(월) 4h15 일단 수기로 작성 ㅠㅠ
    default MyPlanDetailResponseDto toMyPlanDetailResponseDto(Plan entity) {
        if (entity == null) {
            return null;
        }

        MyPlanDetailResponseDto.MyPlanDetailResponseDtoBuilder myPlanDetailResponseDto = MyPlanDetailResponseDto.builder();

        myPlanDetailResponseDto.planId(entity.getPlanId());
        myPlanDetailResponseDto.isMeasurable(entity.getIsMeasurable());
        myPlanDetailResponseDto.object(entity.getObject());
        myPlanDetailResponseDto.totalQuantity(entity.getTotalQuantity());
        myPlanDetailResponseDto.unit(entity.getUnit());

        myPlanDetailResponseDto.hasStartDate(entity.getHasStartDate());
        myPlanDetailResponseDto.startDate(entity.getStartDate());
        myPlanDetailResponseDto.startYear(entity.getStartDate().getYear());
        myPlanDetailResponseDto.startMonth(entity.getStartDate().getMonthValue());

        if (entity.getHasDeadline()) {
            myPlanDetailResponseDto.deadlineDate(entity.getDeadlineDate());
            myPlanDetailResponseDto.deadlineYear(entity.getDeadlineDate().getYear());
            myPlanDetailResponseDto.deadlineMonth(entity.getDeadlineDate().getMonthValue());
        } else {
            ActionDate lastActionDate = entity.getActionDatesList().get(entity.getActionDatesList().size() - 1);
            LocalDate deadlineDate = LocalDate.of(Integer.valueOf(lastActionDate.getNumOfYear()), lastActionDate.getNumOfMonth(), Integer.parseInt(lastActionDate.getNumOfDate()));
            myPlanDetailResponseDto.deadlineDate(deadlineDate);
            myPlanDetailResponseDto.deadlineYear(deadlineDate.getYear());
            myPlanDetailResponseDto.deadlineMonth(deadlineDate.getMonthValue());
        }

        myPlanDetailResponseDto.hasDeadline(entity.getHasDeadline());

        myPlanDetailResponseDto.frequencyDetail(entity.getFrequencyDetail());
        myPlanDetailResponseDto.totalDurationDays(entity.getTotalDurationDays());
        myPlanDetailResponseDto.totalNumOfActions(entity.getTotalNumOfActions());
        myPlanDetailResponseDto.quantityPerDay(entity.getQuantityPerDay());
        myPlanDetailResponseDto.actionDatesList(actionDateListToActionDateResponseDtoList(entity.getActionDatesList()));
        myPlanDetailResponseDto.status(entity.getStatus());

        myPlanDetailResponseDto.lastStatusChangedAt(entity.getLastStatusChangedAt());
        myPlanDetailResponseDto.isChild(entity.getIsChild());
        myPlanDetailResponseDto.sizeOfModifiedPlansList(entity.getModifiedPlans().size());

        return myPlanDetailResponseDto.build();
    }

    default ActionDateResponseDto actionDateToActionDateResponseDto(ActionDate actionDate) {
        if (actionDate == null) {
            return null;
        }

        ActionDateResponseDto.ActionDateResponseDtoBuilder actionDateResponseDto = ActionDateResponseDto.builder();

        actionDateResponseDto.actionDateId(actionDate.getActionDateId());
        actionDateResponseDto.numOfYear(actionDate.getNumOfYear());
        if (actionDate.getNumOfMonth() != null) {
            actionDateResponseDto.numOfMonth(String.valueOf(actionDate.getNumOfMonth()));
        }
        actionDateResponseDto.numOfDate(actionDate.getNumOfDate());
        actionDateResponseDto.numOfDay(actionDate.getNumOfDay());
        actionDateResponseDto.dateFormat(actionDate.getDateFormat());
        actionDateResponseDto.dateType(actionDate.getDateType());
        actionDateResponseDto.schedule(actionDate.getSchedule());
        actionDateResponseDto.memo(actionDate.getMemo());
        actionDateResponseDto.planActionQuantity(actionDate.getPlanActionQuantity());
        actionDateResponseDto.isDone(actionDate.getIsDone());
        actionDateResponseDto.realActionQuantity(actionDate.getRealActionQuantity());
        actionDateResponseDto.reviewScore(actionDate.getReviewScore());
        actionDateResponseDto.realActionDate(actionDate.getRealActionDate());

        return actionDateResponseDto.build();
    }

    default List<ActionDateResponseDto> actionDateListToActionDateResponseDtoList(List<ActionDate> list) {
        if (list == null) {
            return null;
        }

        List<ActionDateResponseDto> list1 = new ArrayList<ActionDateResponseDto>(list.size());
        for (ActionDate actionDate : list) {
            list1.add(actionDateToActionDateResponseDto(actionDate));
        }

        return list1;
    }
    ///////// 구현 클래스에서 가져옴 ㅠㅠ /////////

    // 2023.7.30(일) 21h5
    List<MyPlanDetailResponseDto> toMyPlanDetailResponseDtos(List<Plan> entities);

    // 2023.8.20(일) 21h10
    MainPageResponseDto toMainPageResponseDto(MyPlanDetailResponseDto thisPlan, MyPlanStatisticDetailResponseDto thisStat);
}
