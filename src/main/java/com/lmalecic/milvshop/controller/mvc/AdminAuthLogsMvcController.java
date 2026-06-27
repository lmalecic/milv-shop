package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.service.AuthLogService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/auth-logs")
@RequiredArgsConstructor
public class AdminAuthLogsMvcController {

    private static final String INDEX_URI = "/admin/auth-logs";
    private static final String MODEL_LIST_FRAGMENT = "/fragments/admin/auth-logs/list";

    private final AuthLogService authLogService;

    @GetMapping
    public String getIndexView(Model model, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        model.addAttribute("authLogs", this.authLogService.findAll());

        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setPushUrl(requestUri);
            return "/fragments/admin/auth-logs/index";
        }

        model.addAttribute("sectionUrl", requestUri);
        return AdminMvcController.INDEX_VIEW;
    }
}
