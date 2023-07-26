package org.knou.keyproject.domain.member.service;

import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;

public interface MemberService {
    boolean checkDuplicateEmail(String checkEmail);

    boolean checkDuplicateNickname(String checkNickname);

    Long createMember(MemberPostRequestDto requestDto);
}
