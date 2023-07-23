package org.knou.keyproject.domain.plan.slicetest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knou.keyproject.domain.plan.entity.FrequencyType;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// 2023.7.22(토) 1h55
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PlanRepositoryTest {
    @Autowired
    PlanRepository planRepository;

    @AfterEach
    public void cleanUp() {
        planRepository.deleteAll();
    }

    @Test
    @DisplayName("계획 저장 및 불러오기")
    public void saveAndFindPlanTest() {
        // given
        String object = "자바의 정석 완독";
        String frequencyDetail = "월화수목금토";

        planRepository.save(Plan.builder()
                .isMeasurable(true)
                .object(object)
                .frequencyType(FrequencyType.DATE)
                .frequencyDetail(frequencyDetail)
                .hasDeadline(false)
                .status(PlanStatus.ACTIVE)
                .build());

        // when
        List<Plan> planList = planRepository.findAll();

        // then
        Plan plan = planList.get(0);
        assertThat(plan.getObject()).isEqualTo(object);
        assertThat(plan.getFrequencyDetail()).isEqualTo(frequencyDetail);
    }

    // 2023.7.22(토) 2h10
    @Test
    @DisplayName("BaseTimeEntity 등록")
    public void saveBaseTimeEntityTest() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 7, 22, 2, 15, 0);
        planRepository.save(Plan.builder()
                .isMeasurable(true)
                .object("정보처리기사 실기 합격")
                .frequencyType(FrequencyType.TIMES)
                .frequencyDetail("주 6회")
                .hasDeadline(false)
                .status(PlanStatus.ACTIVE)
                .build());

        // when
        List<Plan> planList = planRepository.findAll();

        // then
        Plan plan = planList.get(0);
        System.out.println(">>>>>>> createdDate = " + plan.getCreatedAt() + ", lastModifiedDate = " + plan.getLastModifiedAt()); // >>>>>>> createdDate = 2023-07-22T02:19:30.113246, lastModifiedDate = 2023-07-22T02:19:30.113246
        assertThat(plan.getCreatedAt().isAfter(now));
        assertThat(plan.getLastModifiedAt().isAfter(now));
    }
}
