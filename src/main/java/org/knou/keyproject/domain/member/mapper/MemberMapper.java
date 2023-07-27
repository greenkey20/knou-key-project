package org.knou.keyproject.domain.member.mapper;

import org.knou.keyproject.domain.member.dto.MemberLoginRequestDto;
import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;
import org.knou.keyproject.domain.member.dto.MemberResponseDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.entity.MemberPlatform;
import org.knou.keyproject.domain.member.entity.MemberStatus;
import org.knou.keyproject.domain.member.entity.Role;
import org.mapstruct.Mapper;

// 2023.7.28(금) 1h30
@Mapper(componentModel = "spring")
public interface MemberMapper {
    default Member toEntity(MemberPostRequestDto memberPostRequestDto) {
        return Member.builder()
                .email(memberPostRequestDto.getEmail())
                .nickname(memberPostRequestDto.getNickname())
                .password(memberPostRequestDto.getPassword())
                .age(memberPostRequestDto.getAge())
                .gender(memberPostRequestDto.getGender())
                .memberPlatform(MemberPlatform.WEBSITE)
                .role(Role.USER)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    Member toEntity(MemberLoginRequestDto memberLoginRequestDto);

    MemberResponseDto.AfterLoginMemberDto toAfterLoginMemberDto(Member entity);
}
