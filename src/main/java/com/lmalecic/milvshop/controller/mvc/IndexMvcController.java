package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.repository.NationRepository;
import com.lmalecic.milvshop.repository.TankRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexMvcController {

    private final NationRepository nationRepository;
    private final TankRoleRepository tankRoleRepository;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("nationsList", nationRepository.findAll());
        model.addAttribute("tankRolesList", tankRoleRepository.findAll());
        return "/index";
    }
}
