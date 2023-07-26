package org.knou.keyproject.domain.member.repository;

import org.knou.keyproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 2023.7.23(일) 21h25
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String checkEmail);

    Optional<Member> findByNickname(String checkNickname);
}
