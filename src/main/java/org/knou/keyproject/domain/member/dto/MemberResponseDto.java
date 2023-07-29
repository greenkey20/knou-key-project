package org.knou.keyproject.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.plan.dto.MyPlanListResponseDto;

import java.util.List;

// 2023.7.27(목) 18h
public class MemberResponseDto {

    // 2023.7.27(목) 18h40
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AfterLoginMemberDto{
        private Long memberId;
        private String nickname;
        private List<MyPlanListResponseDto> planDtoList;
        private List<ActionDateResponseDto> actionDatesList;
    }
}
