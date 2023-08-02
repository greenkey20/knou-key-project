//package org.knou.keyproject.domain.plan.slicetest;
//
//import com.google.gson.Gson;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
//import org.knou.keyproject.domain.plan.entity.Plan;
//import org.knou.keyproject.domain.plan.service.PlanService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.time.LocalDate;
//
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.startsWith;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//// 2023.7.23(일) 22h10
//@SpringBootTest
//@AutoConfigureMockMvc
//public class PlanControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private Gson gson;
//
//    @MockBean
//    private PlanService planService;
//
//    @Test
//    @DisplayName("활동 계획 계산 테스트")
//    public void postNewPlanTest() throws Exception {
//        // given
////        PlanPostRequestDto requestDto = PlanPostRequestDto.builder()
//////                .plannerId(null)
////                .isMeasurableNum(1)
////                .object("자바의 정석 완독")
////                .totalQuantity(998L)
////                .unit("페이지")
////                .startDate(LocalDate.of(2023, 7, 23))
////                .frequencyTypeNum(1)
////                .frequencyDetail("월화수목금")
////                .hasDeadline(1)
////                .deadlineTypeNum(2)
////                .deadlinePeriod("40일")
////                .build();
//
//        // 코드스테이츠 '슬라이스 테스트' 학습 자료 참고
//        /*
//        String content = gson.toJson(requestDto);
//
//        // when
//        ResultActions actions = mockMvc.perform(
//                post("newPlanInsert.pl")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content)
//        );
//
//        // then
//        actions.andExpect(status().isCreated())
//                .andExpect(header().string("Location", is(startsWith("newPlanInsert.pl"))));
//         */
//
//        // 2023.7.23(일) 23h 코드스테이츠 'mockito' 학습 자료 참고
////        Plan plan = requestDto.toEntity();
////        plan.setPlanId(1L);
//    }
//
//}
