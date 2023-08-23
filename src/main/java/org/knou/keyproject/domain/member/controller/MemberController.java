package org.knou.keyproject.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.knou.keyproject.domain.member.dto.MemberLoginRequestDto;
import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.mapper.MemberMapper;
import org.knou.keyproject.domain.member.service.MemberService;
import org.knou.keyproject.domain.plan.dto.MyPlanDetailResponseDto;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

// 2023.7.24(월) 15h5
@Slf4j
//@Transactional/*(propagation = Propagation.REQUIRED, readOnly = true)*/
@RequiredArgsConstructor
@Validated
@Controller
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final PlanMapper planMapper;
    private final PlanService planService;

    @RequestMapping(value = "loginPage.me", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request) {
        // 로그인 후 이전 페이지로 되돌아가기 위해 Referer 헤더 값을 session의 prevPage 속성 값으로 저장
        String referer = request.getHeader("Referer");
        log.info("loginPage() 컨트롤러 메서드에서 referer = " + referer);

        if (referer != null && !referer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referer);
        }

        // 2023.7.28(금) 16h30 비회원이 계산 도중(거의 마지막 단계)에 결과 저장을 위해 회원 가입 + 로그인하는 경우 다시 그 결과로 돌려보내고 싶어서 추가
        if (referer.contains("newPlanInsert") || referer.contains("newPlanByChatGptInsert")) {
            request.getSession().setAttribute("status", "in calculation");
        }

        return "member/login";
    }

    @RequestMapping(value = "login.me", method = RequestMethod.POST)
    public ModelAndView loginMember(@ModelAttribute("member") MemberLoginRequestDto requestDto, ModelAndView mv, HttpSession session) {
        // 2023.7.27(목) 0h10 로직 구현
        log.info("로그인 처리할 컨트롤러에 들어온 정보 = " + requestDto);
        log.info("로그인 처리할 컨트롤러 메서드에 들어옴");

        Member loginMember = memberService.loginMember(requestDto);

        if (loginMember != null) {
//            Hibernate.initialize(loginMember.getPlanList()); // 영속성 컨텍스트가 없는 상황에서 연관관계 있는 데이터를 읽으려고 하는 바, lazy fetch 불가능하다는 오류 -> 이렇게 initialize해서 데이터 읽어올 수 있도록 함

            // 2023.8.7(월) 15h5 수정
            Long memberId = loginMember.getMemberId();
            List<MyPlanDetailResponseDto> planList = planService.findAllActivePlansByMemberMemberId(memberId);
            session.setAttribute("loginUser", memberMapper.toAfterLoginMemberDto(loginMember));
            session.setAttribute("planList", planList);
            session.setAttribute("alertMsg", loginMember.getNickname() + " 님, 어서 오세요!\n오늘 하루도 건강하고 즐겁게 보내보아요!"); // 2023.7.28(금) 23h50 현재 이 alert창 안 뜸
//            mv.setViewName("redirect:/");
        } else {
            mv.addObject("errorMsg", "로그인에 실패했습니다").setViewName("common/error");
            return mv;
        }

        String prevPage = (String) session.getAttribute("prevPage");
        log.info("loginMember() 컨트롤러 메서드에서 prevPage = " + prevPage); // 0h40 현재 loginMember 컨트롤러 메서드에서 prevPage = http://localhost:8080/ 찍히는데, 왜 아래와 같이 분기 시 로그인 후 http://localhost:8080/null로 가는 걸까?
        // 2023.7.27(목) 16h5 테스트 시에는 http://localhost:8080/newPlanInsert.pl 찍힘
//        if (prevPage != null && !prevPage.equals("")) {
//            if (prevPage.contains("newPlanInsert")) {
//                mv.setViewName("redirect:myNewPlanInsertAfterLogin.pl");
//            /*} else if (!prevPage.contains("login")) {
//                mv.setViewName("redirect:" + prevPage);*/
//            } else {
//                mv.setViewName("redirect:/");
//            }
//
//            session.removeAttribute("prevPage");
//        } else {
//            mv.setViewName("redirect:/");
//        }

        // 2023.7.28(금) 16h35 로직 수정해봄
        String statusBeforeLogin = (String) session.getAttribute("status");
        if (statusBeforeLogin != null) {
            if (statusBeforeLogin.equals("in calculation")) {
                mv.setViewName("redirect:myNewPlanInsertAfterLogin.pl");
            } else {
                mv.setViewName("redirect:/mainPage.cm");
            }
        } else {
            mv.setViewName("redirect:/mainPage.cm");
        }

        return mv;
    }

    @GetMapping("findIdPw.me")
    public String findIdPw() {
        return "member/findIdPw";
    }

    @GetMapping("enroll.me")
    public String memberEnrollForm() {
        return "member/memberEnrollForm";
    }

    @RequestMapping(value = "newMemberInsert.me", method = RequestMethod.POST)
    public ModelAndView postMember(@ModelAttribute("member") MemberPostRequestDto requestDto, HttpSession session, ModelAndView mv) {
//        log.info("회원 가입 처리할 컨트롤러에 들어온 정보 = " + requestDto); // 회원 가입 처리할 컨트롤러에 들어온 정보 = MemberPostRequestDto{email='green@gmail.com', nickname='greensoy', password='qwer1234^', age=38, gender=FEMALE}
//        log.info("회원 가입 처리할 컨트롤러 메서드에 들어옴");
        Long memberId = memberService.createMember(requestDto);
//        return new ResponseEntity(memberId, HttpStatus.CREATED); // 이번 프로젝트처럼 화면을 서버에서 넘겨줄 때는 사용하기 부적합

        if (memberId != null) {
//            session.setAttribute("alertMsg", "성공적으로 회원 가입이 되었습니다!");
            session.setAttribute("joinMemberId", memberId);
            mv.setViewName("redirect:/mainPage.cm");
        } else {
            mv.addObject("errorMsg", "회원 가입에 실패했습니다").setViewName("common/error");
        }

        return mv;
    }

    @ResponseBody
    @RequestMapping("idCheck.me")
    public String ajaxCheckDuplicateEmail(String checkEmail) {
        return memberService.checkDuplicateEmail(checkEmail) ? "N" : "Y"; // 중복 검사의 참 = 중복 있음 = 사용할 수 없음
    }

    @ResponseBody
    @RequestMapping("nicknameCheck.me")
    public String ajaxCheckDuplicateNickname(String checkNickname) {
        return memberService.checkDuplicateNickname(checkNickname) ? "N" : "Y";
    }

    // 2023.7.27(목) 12h10
    @RequestMapping("logout.me")
    public String logoutMember(HttpSession session) {
        session.invalidate();
        return "redirect:/mainPage.cm";
    }

    @RequestMapping("myPage.me")
    public String myPage(HttpServletRequest request) {
//        request.getSession().getAttribute("loginUser")
        return "member/myPage";
    }

    // 2023.8.24(목) 1h35 틀만 추가해둠
    @GetMapping("updateMemberInfo.me")
    public String updateMemberInfo(Model model) {

        return "member/updateMemberInfoForm";
    }
}
