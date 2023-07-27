package org.knou.keyproject.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.knou.keyproject.domain.member.entity.*;

// 2023.7.26(수) 22h55
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPostRequestDto {
    @Email
    private String email;
    private String nickname;
    private String password;

    @Positive
    private Integer age;
    private Gender gender;

//    public Member toEntity() {
//        Member member = Member.builder()
//                .email(email)
//                .nickname(nickname)
//                .password(password)
//                .memberPlatform(MemberPlatform.WEBSITE)
//                .age(age)
//                .gender(gender)
//                .role(Role.USER)
//                .status(MemberStatus.ACTIVE)
//                .build();
//
//        return member;
//    }

    @Override
    public String toString() {
        return "MemberPostRequestDto{" +
                "email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }
}
