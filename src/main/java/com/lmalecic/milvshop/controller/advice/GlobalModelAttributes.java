package com.lmalecic.milvshop.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
    }
}
