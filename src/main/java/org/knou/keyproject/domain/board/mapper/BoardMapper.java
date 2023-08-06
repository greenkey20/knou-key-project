package org.knou.keyproject.domain.board.mapper;

import org.knou.keyproject.domain.board.dto.BoardDetailResponseDto;
import org.knou.keyproject.domain.board.dto.BoardListResponseDto;
import org.knou.keyproject.domain.board.entity.Board;
import org.mapstruct.Mapper;

import java.util.List;

// 2023.8.7(월) 0h
@Mapper(componentModel = "spring")
public interface BoardMapper {
    default BoardListResponseDto toBoardListResponseDto(Board board) {
        if (board == null) {
            return null;
        }

        BoardListResponseDto.BoardListResponseDtoBuilder boardListResponseDto = BoardListResponseDto.builder();

        boardListResponseDto.boardId(board.getBoardId());
        boardListResponseDto.boardType(board.getBoardType());
        boardListResponseDto.title(board.getTitle());
        boardListResponseDto.createdAt(board.getCreatedAt().toLocalDate());
        boardListResponseDto.readCount(board.getReadCount());

        boardListResponseDto.planId(board.getPlan().getPlanId());
        boardListResponseDto.object(board.getPlan().getObject());

        boardListResponseDto.memberId(board.getMember().getMemberId());
        boardListResponseDto.nickname(board.getMember().getNickname());

        return boardListResponseDto.build();
    }

    List<BoardListResponseDto> toBoardListResponseDtos(List<Board> boardList);

    default BoardDetailResponseDto toBoardDetailResponseDto(Board board) {
        if (board == null) {
            return null;
        }

        BoardDetailResponseDto.BoardDetailResponseDtoBuilder boardDetailResponseDto = BoardDetailResponseDto.builder();

        boardDetailResponseDto.boardId(board.getBoardId());
        boardDetailResponseDto.boardType(board.getBoardType());
        boardDetailResponseDto.title(board.getTitle());
        boardDetailResponseDto.content(board.getContent());
        boardDetailResponseDto.createdAt(board.getCreatedAt().toLocalDate());
        boardDetailResponseDto.readCount(board.getReadCount());

        boardDetailResponseDto.planId(board.getPlan().getPlanId());
        boardDetailResponseDto.object(board.getPlan().getObject());

        boardDetailResponseDto.memberId(board.getMember().getMemberId());
        boardDetailResponseDto.nickname(board.getMember().getNickname());

        return boardDetailResponseDto.build();
    }
}
