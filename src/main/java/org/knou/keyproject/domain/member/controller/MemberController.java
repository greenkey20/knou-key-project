package org.knou.keyproject.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.authenticator.SavedRequest;
import org.knou.keyproject.domain.member.dto.MemberLoginRequestDto;
import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;
import org.knou.keyproject.domain.member.service.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

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

        if (referer != null && referer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referer);
        }

        return "member/login";
    }

    @RequestMapping(value = "login.me", method = RequestMethod.POST)
    public ModelAndView loginMember(@ModelAttribute("member") MemberLoginRequestDto requestDto, ModelAndView mv, HttpSession session) {


        String prevPage = (String) session.getAttribute("prevPage");
        if (prevPage != null) {
            session.removeAttribute("prevPage");
            mv.setViewName("redirect:" + session.getAttribute("prevPage"));
        } else {
            mv.setViewName("redirect:/");
        }

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
    public void postNewMember(@ModelAttribute("member") MemberPostRequestDto requestDto) {
        log.info("회원 가입 처리할 컨트롤러 메서드에 들어옴");
    }

    @ResponseBody
    @RequestMapping("idCheck.me")
    public String ajaxCheckDuplicateEmail(String checkEmail) {
        return memberService.checkDuplicateEmail(checkEmail) ? "N" : "Y"; // 중복 검사의 참 = 중복 있음 = 사용할 수 없음
    }
}
