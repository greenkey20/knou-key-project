package org.knou.keyproject.domain.main;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.knou.keyproject.domain.member.dto.MemberResponseDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.mapper.MemberMapper;
import org.knou.keyproject.domain.member.service.MemberService;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 2023.7.30(일) 21h45
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class MainController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final PlanMapper planMapper;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String index(HttpSession session) {
        if (session.getAttribute("loginUser") != null) {
            Long memberId = ((MemberResponseDto.AfterLoginMemberDto) session.getAttribute("loginUser")).getMemberId();
            Member loginMember = memberService.findVerifiedMember(memberId);

            if (loginMember != null) {
                Hibernate.initialize(loginMember.getPlanList()); // 영속성 컨텍스트가 없는 상황에서 연관관계 있는 데이터를 읽으려고 하는 바, lazy fetch 불가능하다는 오류 -> 이렇게 initialize해서 데이터 읽어올 수 있도록 함
//            session.setAttribute("loginUser", memberMapper.toAfterLoginMemberDto(loginMember));
                session.setAttribute("planList", planMapper.toMyPlanDetailResponseDtos(loginMember.getPlanList()));
            }
        }

        return "redirect:mainPage.cm";
    }

    @RequestMapping(value = "mainPage.cm", method = RequestMethod.GET)
    public String mainPage(HttpSession session) {
        return "main/main";
    }
}
