package org.knou.keyproject.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.global.utils.MessageSourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static org.knou.keyproject.global.exception.ErrorPageUrl.ERROR_VIEW_NAME;

// 2023.8.20(일) 16h40
@Slf4j
@RequiredArgsConstructor
@Controller
public class ErrorPageController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final MessageSourceUtil messageSource;

    @GetMapping("/common/error/{code}")
    public String error(Model model, @PathVariable(required = false) String code) {
        LOGGER.info(">> ErrorPageController > 에러 발생!");

        model.addAttribute("title", this.messageSource.getMessage("error.page.title"));
        model.addAttribute("description", this.messageSource.getMessage("error.page.desc." + Optional.ofNullable(code).orElseGet(() -> "500")));

        return "redirect:" + ERROR_VIEW_NAME;
    }
}
