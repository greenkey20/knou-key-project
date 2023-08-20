package org.knou.keyproject.global.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

import static org.knou.keyproject.global.exception.ErrorPageUrl.*;

// 2023.8.20(일) 16h
//@Order(1)
//@ControllerAdvice(annotations = Controller.class)
public class GlobalControllerExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    // 400 bad request
    @ExceptionHandler({ConstraintViolationException.class})
    protected String handle400Error(final ConstraintViolationException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("BAD_REQUEST: {}", e.getMessage());
        return "redirect:" + ERROR_400_URL;
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    protected String handle400Error(final DataIntegrityViolationException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("BAD_REQUEST: {}", e.getMessage(), e);
        return "redirect:" + ERROR_400_URL;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    protected String handle400Error(final HttpMessageNotReadableException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("BAD_REQUEST: {}", e.getMessage(), e);
        return "redirect:" + ERROR_400_URL;
    }

    @ExceptionHandler({ConversionFailedException.class})
    protected String handle400Error(final ConversionFailedException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("BAD_REQUEST: {}", e.getMessage(), e);
        return "redirect:" + ERROR_400_URL;
    }

    @ExceptionHandler({ValidationException.class})
    protected String handle400Error(final ValidationException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("BAD_REQUEST: {}", e.getMessage(), e);
        return "redirect:" + ERROR_400_URL;
    }

    @ExceptionHandler({ViolationException.class})
    protected String handle400Error(final ViolationException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("BAD_REQUEST: {}", e.getMessage(), e);
        return "redirect:" + ERROR_400_URL;
    }

    // 404 not found
    @ExceptionHandler({
            EntityNotFoundException.class,
            ResourceNotFoundException.class,
            FileNotFoundException.class})
    protected String handle404Error(final RuntimeException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("NOT_FOUND: {}", e.getMessage(), e);
        return "redirect:" + ERROR_404_URL;
    }

    // 405 method not allowed
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    protected String handle405Error(HttpRequestMethodNotSupportedException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("INTERNAL_SERVER_ERROR: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:" + ERROR_405_URL;
    }

    @ExceptionHandler({InvalidParameterException.class})
    protected String handle405Error(InvalidParameterException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("INTERNAL_SERVER_ERROR: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:" + ERROR_405_URL;
    }

    // 409 conflict
    @ExceptionHandler({
            InvalidDataAccessApiUsageException.class,
            DataAccessException.class})
    protected String handle409Error(final RuntimeException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("CONFLICT: {}", e.getMessage(), e);
        return "redirect:" + ERROR_409_URL;
    }

    // 500 internal server error
    @ExceptionHandler({
            NullPointerException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            RestClientException.class,
            HttpClientErrorException.class,
            HttpServerErrorException.class})
    protected String handle500Error(final RuntimeException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("INTERNAL_SERVER_ERROR: {}", e.getMessage());
        return "redirect:" + ERROR_500_URL;
    }

    @ExceptionHandler({Exception.class})
    protected String handleError(final RuntimeException e, RedirectAttributes redirectAttributes) {
        LOGGER.error("INTERNAL_SERVER_ERROR: {}", e.getMessage());
        return "redirect:" + ERROR_500_URL;
    }
}
