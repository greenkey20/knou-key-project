package org.knou.keyproject.domain.plan;

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
}
