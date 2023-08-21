package org.knou.keyproject.domain.board.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.board.dto.BoardDetailResponseDto;
import org.knou.keyproject.domain.board.dto.BoardListResponseDto;
import org.knou.keyproject.domain.board.dto.BoardPostRequestDto;
import org.knou.keyproject.domain.board.entity.Board;
import org.knou.keyproject.domain.board.mapper.BoardMapper;
import org.knou.keyproject.domain.board.service.BoardService;
import org.knou.keyproject.domain.member.dto.MemberResponseDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.service.MemberService;
import org.knou.keyproject.domain.plan.dto.MyPlanListResponseDto;
import org.knou.keyproject.domain.plan.dto.MyPlanStatisticDetailResponseDto;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 2023.8.5(토) 15h10 클래스 생성
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class BoardController {
    private final BoardService boardService;
    private final PlanService planService;
    private final PlanMapper planMapper;
    private final MemberService memberService;
    private final BoardMapper boardMapper;

    @GetMapping("boardEnrollForm.bd")
    public String boardEnrollForm(@RequestParam(name = "planId") @Positive Long planId,
                                  @RequestParam(name = "planStatus") String status,
                                  HttpSession session,
                                  Model model) {
        log.info("컨트롤러 boardEnrollForm() 메서드에 requestParam으로 들어오는 planId = " + planId + ", status = " + status);

        Long memberId = ((MemberResponseDto.AfterLoginMemberDto) session.getAttribute("loginUser")).getMemberId();
        Member findMember = memberService.findVerifiedMember(memberId);

        Plan findPlan = planService.findVerifiedPlan(planId);
        MyPlanStatisticDetailResponseDto statisticDto = planService.getPlanStatisticDetailById(planId);

        model.addAttribute("plan", planMapper.toMyPlanListResponseDto(findPlan));
        model.addAttribute("statPlan", statisticDto);
//        model.addAttribute("member", memberMapper.toBoardWriterDto(findMember)); // 필요 없음
//        model.addAttribute("status", status);

        return "board/boardEnrollForm";
    }

    @RequestMapping(value = "newBoardInsert.bd", method = RequestMethod.POST)
    public String postNewBoard(@ModelAttribute("board") BoardPostRequestDto requestDto) {
        log.info("컨트롤러 메서드 postNewBoard()로 받은 requestDto = " + requestDto);
        Board board = boardService.saveNewBoard(requestDto);

        return "redirect:boardList.bd";
    }

    @RequestMapping(value = "boardUpdate.bd", method = RequestMethod.POST)
    public String patchBoard(@RequestParam(name = "boardId") @Positive Long boardId,
                             @ModelAttribute("board") BoardPostRequestDto requestDto) {
        log.info("컨트롤러 메서드 patchBoard()로 받은 requestDto = " + requestDto);
        Board board = boardService.updateBoard(boardId, requestDto);

        return "redirect:boardDetail.bd?boardId=" + board.getBoardId();
    }

    @GetMapping("boardList.bd")
    public String getBoardList(@PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(required = false, defaultValue = "") String keyword,
                               Model model) {
        Page<Board> boardList = boardService.findAllBoards(pageable);

        if (keyword != null) {
            boardList = boardService.findByTitleContainingOrContentContaining(keyword, pageable);
        }

        int pageNumber = boardList.getPageable().getPageNumber(); // 현재 페이지
        int totalPages = boardList.getTotalPages(); // 총 페이지 수 = boardList의 size 값
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNumber / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        endBlockPage = Math.min(totalPages, endBlockPage);

        List<Board> list = boardList.getContent();
        List<BoardListResponseDto> responseDtos = boardService.getBoardListResponseDtosList(list);

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);
        model.addAttribute("boardList", boardList);
        model.addAttribute("list", responseDtos);

        return "board/boardListView";
    }

    @GetMapping("boardDetail.bd")
    public String getBoardDetail(@RequestParam(name = "boardId") @Positive Long boardId, Model model) {
        boardService.increaseReadCount(boardId);

        Board findBoard = boardService.findBoardById(boardId);
        BoardDetailResponseDto boardDto = boardService.getBoardDetailResponseDto(boardId);

        Long planId = findBoard.getPlan().getPlanId();
        Plan findPlan = planService.findVerifiedPlan(planId);
        MyPlanStatisticDetailResponseDto statisticDto = planService.getPlanStatisticDetailById(planId);

        model.addAttribute("board", boardDto);
        model.addAttribute("plan", planMapper.toMyPlanListResponseDto(findPlan));
        model.addAttribute("statPlan", statisticDto);

        return "board/boardDetailView";
    }

    // 2023.8.21(월) 17h35 LazyInitializationException: could not initialize proxy [org.knou.keyproject.domain.plan.entity.Plan#3] - no Session 처리
    @GetMapping("boardUpdatePage.bd")
    public String boardUpdatePage(@RequestParam(name = "boardId") @Positive Long boardId, Model model) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetailResponseDto(boardId);
        MyPlanListResponseDto myPlanListResponseDto = planService.getPlanAboutBoard(boardId);
        MyPlanStatisticDetailResponseDto statisticDto = planService.getPlanStatisticDetailAboutBoard(boardId);

        model.addAttribute("board", boardDetailResponseDto);
        model.addAttribute("plan", myPlanListResponseDto);
        model.addAttribute("statPlan", statisticDto);

        return "board/boardUpdatePage";
    }
}
