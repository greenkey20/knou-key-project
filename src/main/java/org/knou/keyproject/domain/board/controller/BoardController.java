package org.knou.keyproject.domain.board.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.board.dto.BoardPostRequestDto;
import org.knou.keyproject.domain.board.entity.Board;
import org.knou.keyproject.domain.board.service.BoardService;
import org.knou.keyproject.domain.member.dto.MemberResponseDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.mapper.MemberMapper;
import org.knou.keyproject.domain.member.service.MemberService;
import org.knou.keyproject.domain.plan.dto.MyPlanStatisticDetailResponseDto;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final MemberMapper memberMapper;

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
        model.addAttribute("member", memberMapper.toBoardWriterDto(findMember));
//        model.addAttribute("status", status);

        return "board/boardEnrollForm";
    }

    @RequestMapping(value = "newBoardInsert.bd", method = RequestMethod.POST)
    public String postNewBoard(@ModelAttribute("board") BoardPostRequestDto requestDto) {
        log.info("컨트롤러 메서드 postNewBoard()로 받은 requestDto = " + requestDto);
        Board board = boardService.saveNewBoard(requestDto);

        return "redirect:boardList.bd";
    }

    @GetMapping("boardList.bd")
    public String getBoardList() {
        return "board/boardListView";
    }
}
