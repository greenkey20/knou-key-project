package org.knou.keyproject.domain.plan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.knou.keyproject.domain.plan.dto.MyPlanDetailResponseDto;
import org.knou.keyproject.domain.plan.dto.MyPlanListResponseDto;
import org.knou.keyproject.domain.plan.dto.MyPlanPostRequestDto;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T18:30:30+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 17.0.7 (Azul Systems, Inc.)"
)
@Component
public class PlanMapperImpl implements PlanMapper {

    @Override
    public Plan toEntity(MyPlanPostRequestDto myPlanPostRequestDto) {
        if ( myPlanPostRequestDto == null ) {
            return null;
        }

        Plan.PlanBuilder plan = Plan.builder();

        plan.planId( myPlanPostRequestDto.getPlanId() );
        plan.startDate( myPlanPostRequestDto.getStartDate() );

        return plan.build();
    }

    @Override
    public MyPlanListResponseDto toMyPlanListResponseDto(Plan entity) {
        if ( entity == null ) {
            return null;
        }

        MyPlanListResponseDto.MyPlanListResponseDtoBuilder myPlanListResponseDto = MyPlanListResponseDto.builder();

        myPlanListResponseDto.planId( entity.getPlanId() );
        myPlanListResponseDto.isChild( entity.getIsChild() );
        myPlanListResponseDto.object( entity.getObject() );
        myPlanListResponseDto.isMeasurable( entity.getIsMeasurable() );
        myPlanListResponseDto.status( entity.getStatus() );
        myPlanListResponseDto.startDate( entity.getStartDate() );
        myPlanListResponseDto.hasDeadline( entity.getHasDeadline() );
        myPlanListResponseDto.deadlineDate( entity.getDeadlineDate() );
        myPlanListResponseDto.totalDurationDays( entity.getTotalDurationDays() );
        myPlanListResponseDto.totalNumOfActions( entity.getTotalNumOfActions() );
        myPlanListResponseDto.totalQuantity( entity.getTotalQuantity() );
        myPlanListResponseDto.frequencyDetail( entity.getFrequencyDetail() );
        myPlanListResponseDto.quantityPerDay( entity.getQuantityPerDay() );
        myPlanListResponseDto.unit( entity.getUnit() );

        return myPlanListResponseDto.build();
    }

    @Override
    public List<MyPlanDetailResponseDto> toMyPlanDetailResponseDtos(List<Plan> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MyPlanDetailResponseDto> list = new ArrayList<MyPlanDetailResponseDto>( entities.size() );
        for ( Plan plan : entities ) {
            list.add( toMyPlanDetailResponseDto( plan ) );
        }

        return list;
    }
}
