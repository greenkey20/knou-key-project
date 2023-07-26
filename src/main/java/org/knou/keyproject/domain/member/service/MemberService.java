package org.knou.keyproject.domain.member.service;

import org.knou.keyproject.domain.member.dto.MemberLoginRequestDto;
import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;
import org.knou.keyproject.domain.member.entity.Member;

public interface MemberService {
    boolean checkDuplicateEmail(String checkEmail);

    boolean checkDuplicateNickname(String checkNickname);

    Long createMember(MemberPostRequestDto requestDto);

    Member loginMember(MemberLoginRequestDto requestDto);
}
