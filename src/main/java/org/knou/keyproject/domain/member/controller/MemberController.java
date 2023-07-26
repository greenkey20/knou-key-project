package org.knou.keyproject.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.dto.MemberLoginRequestDto;
import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

// 2023.7.24(월) 15h5
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class MemberController {
    private final MemberService memberService;

    @RequestMapping(value = "loginPage.me", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request) {
        // 로그인 후 이전 페이지로 되돌아가기 위해 Referer 헤더 값을 session의 prevPage 속성 값으로 저장
        String referer = request.getHeader("Referer");

        if (referer != null && !referer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referer);
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
            session.setAttribute("loginUser", loginMember);
            session.setAttribute("alertMsg", loginMember.getNickname() + " 님, 어서 오세요!\n오늘 하루도 건강하고 즐겁게 보내보아요!");
            mv.setViewName("redirect:/");
        } else {
            mv.addObject("errorMsg", "로그인 실패").setViewName("common/errorPage");
        }

        String prevPage = (String) session.getAttribute("prevPage");
        log.info("loginMember 컨트롤러 메서드에서 prevPage = " + prevPage); // 0h40 현재 loginMember 컨트롤러 메서드에서 prevPage = http://localhost:8080/ 찍히는데, 왜 아래와 같이 분기 시 로그인 후 http://localhost:8080/null로 가는 걸까?
//        if (prevPage != null) {
//            session.removeAttribute("prevPage");
//            mv.setViewName("redirect:" + session.getAttribute("prevPage"));
//        } else {
//            mv.setViewName("redirect:/");
//        }

        return mv;
    }

    @GetMapping("findIdPw.me")
    public String findIdPw() {
        return "member/findIdPw";
    }

    @GetMapping("enroll.me")
    public String enroll() {
        return "member/memberEnrollForm";
    }

    @RequestMapping(value = "newMemberInsert.me", method = RequestMethod.POST)
    public String postMember(@ModelAttribute("member") MemberPostRequestDto requestDto, HttpSession session) {
//        log.info("회원 가입 처리할 컨트롤러에 들어온 정보 = " + requestDto); // 회원 가입 처리할 컨트롤러에 들어온 정보 = MemberPostRequestDto{email='green@gmail.com', nickname='greensoy', password='qwer1234^', age=38, gender=FEMALE}
//        log.info("회원 가입 처리할 컨트롤러 메서드에 들어옴");
        Long memberId = memberService.createMember(requestDto);
//        return new ResponseEntity(memberId, HttpStatus.CREATED); // 이번 프로젝트처럼 화면을 서버에서 넘겨줄 때는 사용하기 부적합

        session.setAttribute("alertMsg", "성공적으로 회원 가입이 되었습니다!");
        return "redirect:/";
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
}
