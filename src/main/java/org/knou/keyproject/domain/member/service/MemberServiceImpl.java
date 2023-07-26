package org.knou.keyproject.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.global.exception.BusinessLogicException;
import org.knou.keyproject.global.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
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

    private void verifyExistingMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
}
