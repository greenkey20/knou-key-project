package org.knou.keyproject.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knou.keyproject.domain.plan.controller.PlanController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 2023.7.22(토) 16h40
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = RANDOM_PORT)
@WebMvcTest(PlanController.class)
public class IndexControllerTest {
    @Autowired
//    private TestRestTemplate restTemplate;
    MockMvc mvc;

    @Test
    @DisplayName("메인페이지 로딩")
    public void getIndexPage() throws Exception {
        // given
        String body = "hello, Spring Boot app with JSP!";

        // when
//        String body = this.restTemplate.getForObject("/", String.class);

        // then
//        assertThat(body).contains("with JSP");
        mvc.perform(get("/"))
                .andExpect(status().isOk())
//                .andExpect(content().string("hello, Spring Boot app with JSP!"))
        ;
    }
}
