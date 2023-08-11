package org.knou.keyproject.domain.member.mapper;

import javax.annotation.processing.Generated;
import org.knou.keyproject.domain.member.dto.MemberLoginRequestDto;
import org.knou.keyproject.domain.member.dto.MemberResponseDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T18:30:30+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 17.0.7 (Azul Systems, Inc.)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member toEntity(MemberLoginRequestDto memberLoginRequestDto) {
        if ( memberLoginRequestDto == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.memberId( memberLoginRequestDto.getMemberId() );
        member.email( memberLoginRequestDto.getEmail() );
        member.password( memberLoginRequestDto.getPassword() );

        return member.build();
    }

    @Override
    public MemberResponseDto.AfterLoginMemberDto toAfterLoginMemberDto(Member entity) {
        if ( entity == null ) {
            return null;
        }

        MemberResponseDto.AfterLoginMemberDto.AfterLoginMemberDtoBuilder afterLoginMemberDto = MemberResponseDto.AfterLoginMemberDto.builder();

        afterLoginMemberDto.memberId( entity.getMemberId() );
        afterLoginMemberDto.nickname( entity.getNickname() );
        afterLoginMemberDto.profileImageUrl( entity.getProfileImageUrl() );
        afterLoginMemberDto.status( entity.getStatus() );

        return afterLoginMemberDto.build();
    }

    @Override
    public MemberResponseDto.BoardWriterDto toBoardWriterDto(Member entity) {
        if ( entity == null ) {
            return null;
        }

        MemberResponseDto.BoardWriterDto.BoardWriterDtoBuilder boardWriterDto = MemberResponseDto.BoardWriterDto.builder();

        boardWriterDto.memberId( entity.getMemberId() );
        boardWriterDto.nickname( entity.getNickname() );

        return boardWriterDto.build();
    }
}
