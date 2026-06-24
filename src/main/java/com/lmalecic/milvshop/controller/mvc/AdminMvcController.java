package com.lmalecic.milvshop.controller.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminMvcController {

    public static final String INDEX_VIEW = "admin";

    @GetMapping
    public String getIndexView(Model model) {
        model.addAttribute("sectionUrl", null);
        return INDEX_VIEW;
    }
}
