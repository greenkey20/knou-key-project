package org.knou.keyproject.domain.board.mapper;

import org.knou.keyproject.domain.board.dto.BoardListResponseDto;
import org.knou.keyproject.domain.board.entity.Board;
import org.mapstruct.Mapper;

import java.util.List;

// 2023.8.7(월) 0h
@Mapper(componentModel = "spring")
public interface BoardMapper {
    default BoardListResponseDto toBoardResponseDto(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardListResponseDto.BoardListResponseDtoBuilder boardListResponseDto = BoardListResponseDto.builder();

        boardListResponseDto.boardId( board.getBoardId() );
        boardListResponseDto.boardType( board.getBoardType() );
        boardListResponseDto.title( board.getTitle() );
        boardListResponseDto.createdAt( board.getCreatedAt().toLocalDate() ) ;
        boardListResponseDto.readCount( board.getReadCount() );

        boardListResponseDto.planId(board.getPlan().getPlanId());
        boardListResponseDto.object(board.getPlan().getObject());

        boardListResponseDto.memberId(board.getMember().getMemberId());
        boardListResponseDto.nickname(board.getMember().getNickname());

        return boardListResponseDto.build();
    }

    List<BoardListResponseDto> toBoardResponseDtos(List<Board> boardList);
}
