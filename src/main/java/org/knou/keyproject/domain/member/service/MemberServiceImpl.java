package org.knou.keyproject.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public boolean checkDuplicateEmail(String checkEmail) {
        Member member = memberRepository.findByEmail(checkEmail);

        if (member != null) { // 중복된 이메일이 있음 = 중복 검사의 참 = 사용 불가능
            return true;
        }

        return false;
    }
}
