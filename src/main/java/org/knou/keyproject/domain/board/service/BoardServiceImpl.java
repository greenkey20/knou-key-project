package org.knou.keyproject.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .content(requestDto.getContent())
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
