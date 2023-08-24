package org.knou.keyproject.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.board.dto.BoardDetailResponseDto;
import org.knou.keyproject.domain.board.dto.BoardListResponseDto;
import org.knou.keyproject.domain.board.dto.BoardPostRequestDto;
import org.knou.keyproject.domain.board.entity.Board;
import org.knou.keyproject.domain.board.entity.BoardType;
import org.knou.keyproject.domain.board.repository.BoardRepository;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.service.MemberService;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.knou.keyproject.global.utils.StringParsingUtils.replaceNewLineWithBr;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final PlanService planService;

    @Override
    @Transactional
    public Board saveNewBoard(BoardPostRequestDto requestDto) {
        Member findMember = memberService.findVerifiedMember(requestDto.getMemberId());
        Plan findPlan = planService.findVerifiedPlan(requestDto.getPlanId());

        Board boardToSave = Board.builder()
                .title(requestDto.getTitle())
                .content(replaceNewLineWithBr(requestDto.getContent()))
                .boardType(getBoardTypeFromString(requestDto.getBoardType()))
                .member(findMember)
                .plan(findPlan)
                .build();

        return boardRepository.save(boardToSave);
    }

    @Override
    @Transactional
    public Board updateBoard(Long boardId, BoardPostRequestDto requestDto) {
        Board findBoard = findVerifiedBoard(boardId);

        findBoard.updateTitle(requestDto.getTitle());
        findBoard.updateContent(requestDto.getContent());

        return findBoard;
    }

    // 2023.8.7(월) 16h55
    @Override
    public List<BoardListResponseDto> getBoardListResponseDtosList(List<Board> list) {
        List<BoardListResponseDto> boardListResponseDtos = new ArrayList<>();

        for (Board board : list) {
            Long boardId = board.getBoardId();
            Board findBoard = findVerifiedBoard(boardId);

            BoardListResponseDto.BoardListResponseDtoBuilder boardListResponseDto = BoardListResponseDto.builder();
            boardListResponseDto.boardId(boardId);
            boardListResponseDto.boardType(findBoard.getBoardType());
            boardListResponseDto.title(findBoard.getTitle());
            boardListResponseDto.createdAt(findBoard.getCreatedAt().toLocalDate());
            boardListResponseDto.readCount(findBoard.getReadCount());

            Plan findPlan = planService.findVerifiedPlan(findBoard.getPlan().getPlanId());
            boardListResponseDto.planId(findPlan.getPlanId());
            boardListResponseDto.object(findPlan.getObject());

            Member findMember = memberService.findVerifiedMember(findBoard.getMember().getMemberId());
            boardListResponseDto.memberId(findMember.getMemberId());
            boardListResponseDto.nickname(findMember.getNickname());

            boardListResponseDtos.add(boardListResponseDto.build());
        }

        return boardListResponseDtos;
    }

    // 2023.8.7(월) 17h15
    @Override
    public BoardDetailResponseDto getBoardDetailResponseDto(Long boardId) {
        Board findBoard = findVerifiedBoard(boardId);

        BoardDetailResponseDto.BoardDetailResponseDtoBuilder boardDetailResponseDto = BoardDetailResponseDto.builder();

        boardDetailResponseDto.boardId(boardId);
        boardDetailResponseDto.boardType(findBoard.getBoardType());
        boardDetailResponseDto.title(findBoard.getTitle());
        boardDetailResponseDto.content(findBoard.getContent());
        boardDetailResponseDto.createdAt(findBoard.getCreatedAt().toLocalDate());
        boardDetailResponseDto.readCount(findBoard.getReadCount());

        Plan findPlan = planService.findVerifiedPlan(findBoard.getPlan().getPlanId());
        boardDetailResponseDto.planId(findPlan.getPlanId());
        boardDetailResponseDto.object(findPlan.getObject());

        Member findMember = memberService.findVerifiedMember(findBoard.getMember().getMemberId());
        boardDetailResponseDto.memberId(findMember.getMemberId());
        boardDetailResponseDto.nickname(findMember.getNickname());

        return boardDetailResponseDto.build();
    }

    @Override
    public Page<Board> findAllByMemberMemberIdOrderByBoardIdDesc(Long memberId, Pageable pageable) {
        Member findMember = memberService.findVerifiedMember(memberId);
        return boardRepository.findAllByMemberMemberIdOrderByBoardIdDesc(findMember.getMemberId(), pageable);
    }

    // 2023.8.6(일) 23h45
    @Override
    public Page<Board> findAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 2023.8.6(일) 23h50
    public Page<Board> findByTitleContainingOrContentContaining(String keyword, Pageable pageable) {
        return boardRepository.findByTitleOrContentKeyword(keyword, pageable);
    }

    @Override
    public Board findBoardById(Long boardId) {
        return findVerifiedBoard(boardId);
    }

    private BoardType getBoardTypeFromString(String boardTypeStr) {
        switch (boardTypeStr) {
            case "PLAN":
                return BoardType.PLAN;
            case "SUCCESS":
                return BoardType.SUCCESS;
        }

        return null;
    }

    @Override
    public Board findVerifiedBoard(Long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }

    @Override
    @Transactional
    public void increaseReadCount(Long boardId) {
        Board findBoard = findVerifiedBoard(boardId);
        findBoard.increaseReadCount();
    }
}
