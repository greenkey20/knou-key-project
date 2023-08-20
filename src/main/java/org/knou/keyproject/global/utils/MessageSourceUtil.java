package org.knou.keyproject.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;

// 2023.8.20(일) 16h50
@Component
@RequiredArgsConstructor
public class MessageSourceUtil {
    private final MessageSource messageSource;

    public String getMessage(final String code, @Nullable final Object... args) {
        return this.getMessage(code, LocaleContextHolder.getLocale(), args);
    }

    public String getMessage(final String code, final Locale locale, @Nullable final Object... args) {
        return this.messageSource.getMessage(code, args, locale);
    }
}
