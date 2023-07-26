package org.knou.keyproject.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.dto.MemberLoginRequestDto;
import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.knou.keyproject.global.exception.BusinessLogicException;
import org.knou.keyproject.global.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    @Override

    public boolean checkDuplicateEmail(String checkEmail) {
        Optional<Member> optionalMember = memberRepository.findByEmail(checkEmail);

//        if (member != null) { // 중복된 이메일이 있음 = 중복 검사의 참 = 사용 불가능
//            return true;
//        }
//
//        return false;
        return optionalMember.isPresent();
    }

    @Override
    public boolean checkDuplicateNickname(String checkNickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(checkNickname);
        return optionalMember.isPresent();
    }

    // 2023.7.26(수) 23h10
    @Override
    @Transactional
    public Long createMember(MemberPostRequestDto requestDto) {
        Member member = requestDto.toEntity();
        verifyExistingMember(member.getEmail());

        // Spring Security는 추후 추가하기로 함
//        String encryptedPassword = passwordEncoder.encode(member.getPassword());
//        member.setPassword(encryptedPassword);

        Member savedMember = memberRepository.save(member);
        return savedMember.getMemberId();
    }

    // 2023.7.27(목) 0h20
    @Override
    @Transactional
    public Member loginMember(MemberLoginRequestDto requestDto) {
        Member findMember = findVerifiedMember(requestDto.getEmail());

//        if (encryptedPW == db에 저장된 PW) {
        if (findMember.getPassword().equals(requestDto.getPassword())) {
            findMember.setLastLoginAt(LocalDateTime.now());
            findMember = memberRepository.save(findMember);
            return findMember;
        } else {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_USER);
        }
    }

    /**
     * 중복 회원이 있을 때 예외 처리하는 메서드
     *
     * @param email
     */
    private void verifyExistingMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    private Member findVerifiedMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}
