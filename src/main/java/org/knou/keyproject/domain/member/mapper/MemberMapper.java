package org.knou.keyproject.domain.member.mapper;

import org.knou.keyproject.domain.member.dto.MemberLoginRequestDto;
import org.knou.keyproject.domain.member.dto.MemberPostRequestDto;
import org.knou.keyproject.domain.member.dto.MemberResponseDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.entity.MemberPlatform;
import org.knou.keyproject.domain.member.entity.MemberStatus;
import org.knou.keyproject.domain.member.entity.Role;
import org.mapstruct.Mapper;

import java.time.LocalDate;

// 2023.7.28(금) 1h30
@Mapper(componentModel = "spring")
public interface MemberMapper {
    default Member toEntity(MemberPostRequestDto memberPostRequestDto) {
        int yearOfBirth = 0;
        if (memberPostRequestDto.getAge() != null) {
            yearOfBirth = LocalDate.now().getYear() - memberPostRequestDto.getAge();
        }

        return Member.builder()
                .email(memberPostRequestDto.getEmail())
                .nickname(memberPostRequestDto.getNickname())
                .password(memberPostRequestDto.getPassword())
                .age(memberPostRequestDto.getAge())
                .yearOfBirth(yearOfBirth)
                .gender(memberPostRequestDto.getGender())
                .memberPlatform(MemberPlatform.WEBSITE)
                .role(Role.USER)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    Member toEntity(MemberLoginRequestDto memberLoginRequestDto);

    MemberResponseDto.AfterLoginMemberDto toAfterLoginMemberDto(Member entity);

    MemberResponseDto.BoardWriterDto toBoardWriterDto(Member entity);
}
