package org.knou.keyproject.domain.plan.mapper;

import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.domain.plan.dto.*;
import org.knou.keyproject.domain.plan.entity.DeadlineType;
import org.knou.keyproject.domain.plan.entity.FrequencyType;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 2023.7.28(금) 1h25
@Mapper(componentModel = "spring")
public interface PlanMapper {

//    Plan toEntity(PlanPostRequestDto planPostRequestDto);

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
        newPlanResponseDto.deadlineDate(entity.getDeadlineDate());
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
                System.out.println("----------PlanMapper 여기에서 이미 null이에요-----------");
                return null;
            }

            System.out.println("----------PlanMapper 여기에서 null은 아니에요-----------");

            ActionDateResponseDto.ActionDateResponseDtoBuilder actionDateResponseDto = ActionDateResponseDto.builder();

            actionDateResponseDto.year( thisActionDate.getYear() );
            if ( thisActionDate.getMonth() != null ) {
                actionDateResponseDto.month( String.valueOf( thisActionDate.getMonth() ) );
            }
            actionDateResponseDto.date( thisActionDate.getDate() );
            actionDateResponseDto.day( thisActionDate.getDay() );
            actionDateResponseDto.isDone( thisActionDate.getIsDone() );
            actionDateResponseDto.planActionQuantity( thisActionDate.getPlanActionQuantity() );
            actionDateResponseDto.realActionQuantity( thisActionDate.getRealActionQuantity() );
            actionDateResponseDto.dateFormat( thisActionDate.getDateFormat() );

            actionDates.add(actionDateResponseDto.build());
        }

        // 2023.7.28(금) 4h30 이 줄을 안 써서 1시간 이상 디버깅..
        newPlanResponseDto.actionDates(entity.getActionDatesList());

        return newPlanResponseDto.build();
    }

    MyPlanListResponseDto toMyPlanListResponseDto(Plan entity);

    MyPlanDetailResponseDto toMyPlanDetailResponseDto(Plan entity);
}
