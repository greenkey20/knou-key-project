package org.knou.keyproject.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.plan.dto.MyPlanListResponseDto;
import org.knou.keyproject.domain.plan.entity.ActionDate;

import java.util.List;

// 2023.7.27(목) 18h
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AfterLoginResponseDto {
    private Long memberId;
    private String nickname;
    private List<MyPlanListResponseDto> planDtoList;
    private List<ActionDate> actionDatesList;
}
