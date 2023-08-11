package org.knou.keyproject.domain.board.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.knou.keyproject.domain.board.dto.BoardListResponseDto;
import org.knou.keyproject.domain.board.entity.Board;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T18:30:30+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 17.0.7 (Azul Systems, Inc.)"
)
@Component
public class BoardMapperImpl implements BoardMapper {

    @Override
    public List<BoardListResponseDto> toBoardListResponseDtos(List<Board> boardList) {
        if ( boardList == null ) {
            return null;
        }

        List<BoardListResponseDto> list = new ArrayList<BoardListResponseDto>( boardList.size() );
        for ( Board board : boardList ) {
            list.add( toBoardListResponseDto( board ) );
        }

        return list;
    }
}
