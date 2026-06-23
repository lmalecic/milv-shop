package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.repository.NationRepository;
import com.lmalecic.milvshop.repository.TankRoleRepository;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexMvcController {

    private final NationRepository nationRepository;
    private final TankRoleRepository tankRoleRepository;

    @GetMapping
    public String getIndex(Model model,
                           @RequestParam(required = false) String auth,
                           @RequestParam(required = false) String redirectUrl,
                           HtmxResponse htmxResponse
    ) {
        model.addAttribute("nationsList", this.nationRepository.findAll());
        model.addAttribute("tankRolesList", this.tankRoleRepository.findAll());

        if (auth != null) {
            model.addAttribute("promptAuth", true);
            model.addAttribute("redirectUrl", redirectUrl);
        }

        return "index";
    }
}
