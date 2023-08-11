package org.knou.keyproject.domain.actiondate.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.knou.keyproject.domain.actiondate.dto.ActionDatePostRequestDto;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T18:30:30+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 17.0.7 (Azul Systems, Inc.)"
)
@Component
public class ActionDateMapperImpl implements ActionDateMapper {

    @Override
    public ActionDateResponseDto toActionDateResponseDto(ActionDate entity) {
        if ( entity == null ) {
            return null;
        }

        ActionDateResponseDto.ActionDateResponseDtoBuilder actionDateResponseDto = ActionDateResponseDto.builder();

        actionDateResponseDto.actionDateId( entity.getActionDateId() );
        actionDateResponseDto.numOfYear( entity.getNumOfYear() );
        if ( entity.getNumOfMonth() != null ) {
            actionDateResponseDto.numOfMonth( String.valueOf( entity.getNumOfMonth() ) );
        }
        actionDateResponseDto.numOfDate( entity.getNumOfDate() );
        actionDateResponseDto.numOfDay( entity.getNumOfDay() );
        actionDateResponseDto.dateFormat( entity.getDateFormat() );
        actionDateResponseDto.dateType( entity.getDateType() );
        actionDateResponseDto.schedule( entity.getSchedule() );
        actionDateResponseDto.memo( entity.getMemo() );
        actionDateResponseDto.planActionQuantity( entity.getPlanActionQuantity() );
        actionDateResponseDto.isDone( entity.getIsDone() );
        actionDateResponseDto.realActionQuantity( entity.getRealActionQuantity() );
        actionDateResponseDto.timeTakenForRealAction( entity.getTimeTakenForRealAction() );
        actionDateResponseDto.reviewScore( entity.getReviewScore() );
        actionDateResponseDto.realActionDate( entity.getRealActionDate() );

        return actionDateResponseDto.build();
    }

    @Override
    public List<ActionDateResponseDto> entitiesToDtos(List<ActionDate> entites) {
        if ( entites == null ) {
            return null;
        }

        List<ActionDateResponseDto> list = new ArrayList<ActionDateResponseDto>( entites.size() );
        for ( ActionDate actionDate : entites ) {
            list.add( toActionDateResponseDto( actionDate ) );
        }

        return list;
    }

    @Override
    public ActionDate toEntity(ActionDatePostRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        ActionDate.ActionDateBuilder actionDate = ActionDate.builder();

        actionDate.actionDateId( requestDto.getActionDateId() );
        actionDate.memo( requestDto.getMemo() );
        actionDate.realActionQuantity( requestDto.getRealActionQuantity() );
        actionDate.timeTakenForRealAction( requestDto.getTimeTakenForRealAction() );
        actionDate.reviewScore( requestDto.getReviewScore() );
        actionDate.realActionDate( requestDto.getRealActionDate() );

        return actionDate.build();
    }
}
