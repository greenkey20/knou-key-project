package org.knou.keyproject.global.exception;

import lombok.RequiredArgsConstructor;
import org.knou.keyproject.global.utils.MessageSourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

// 2023.8.20(일) 16h40
//@Slf4j
@RequiredArgsConstructor
@Controller
public class ErrorPageController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final MessageSourceUtil messageSource;

    @GetMapping("/common/error/{code}")
    public ModelAndView error(ModelAndView mv, @PathVariable(required = false) final String code) {
        LOGGER.info(">> ErrorPageController > 에러 발생!");

//        model.addAttribute("title", this.messageSource.getMessage("error.page.title"));
//        model.addAttribute("description", this.messageSource.getMessage("error.page.desc." + Optional.ofNullable(code).orElseGet(() -> "500")));
        LOGGER.info(">> ErrorPageController > code = " + code); // 2023.8.20(일) 19h45 아래 errorPage 경로 잘못 써서 404 에러 발생했을 때 "ErrorPageController > code = 500" 찍힘
        mv.addObject("errorMsg", "처리 중 오류가 발생했습니다").setViewName("common/errorPage");

//        return ERROR_VIEW_NAME;
        return mv;
    }
}
