package com.lmalecic.milvshop.controller.advice;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(NoResourceFoundException ex, Model model, HtmxRequest htmxRequest) {
        model.addAttribute("status", ex.getBody().getStatus());
        model.addAttribute("error", ex.getBody().getTitle());
        model.addAttribute("message", ex.getBody().getDetail());
        return resolveErrorView(htmxRequest);
    }

    private String resolveErrorView(HtmxRequest htmxRequest) {
        return htmxRequest.isHtmxRequest()
                ? "error :: content"
                : "error";
    }
}
