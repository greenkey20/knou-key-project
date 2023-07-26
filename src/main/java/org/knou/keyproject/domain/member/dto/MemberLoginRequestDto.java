package org.knou.keyproject.domain.member.dto;

import lombok.*;

// 2023.7.27(목) 0h10
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberLoginRequestDto {
    private String email;
    private String password;

    @Override
    public String toString() {
        return "MemberLoginRequestDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
