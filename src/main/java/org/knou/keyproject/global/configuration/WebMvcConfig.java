package org.knou.keyproject.global.configuration;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import static org.knou.keyproject.global.exception.ErrorPageUrl.*;

// 2023.8.20(일) 15h50
@Configuration
//@EnableWebMvc
public class WebMvcConfig {
    /**
     * 아래 WebMvcConfig의 에러 페이지 설정과
     * @ControllerAdvice(GlobalControllerExceptionHandler)에 따라
     * 이 App에서 발생하는 대부분의 에러는 /src/main/webapp/WEB-INF/view/common/error.jsp로 redirect됨
     * @return
     */
    @Bean
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> enableDefaultServlet() {
        return (factory) -> {
//            factory.setRegisterDefaultServlet(true); // 나의 경우 이 설정 꼭 해야하는지 잘 모르겠어서 주석 처리함
            factory.addErrorPages(
                    new ErrorPage(HttpStatus.BAD_REQUEST, ERROR_400_URL),
                    new ErrorPage(HttpStatus.UNAUTHORIZED, ERROR_401_URL),
                    new ErrorPage(HttpStatus.FORBIDDEN, ERROR_403_URL),
                    new ErrorPage(HttpStatus.NOT_FOUND, ERROR_404_URL),
                    new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_500_URL)
            );
        };
    }
}
