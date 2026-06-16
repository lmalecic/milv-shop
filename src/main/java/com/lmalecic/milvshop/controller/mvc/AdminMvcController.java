package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TanksSearchCriteria;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.service.NationService;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.service.TankService;
import com.lmalecic.milvshop.util.UrlUtils;
import com.lmalecic.milvshop.viewmodel.TanksViewModel;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMvcController {

    private final TankService tankService;
    private final NationService nationService;
    private final TankRoleService tankRoleService;

    @GetMapping({"", "/"})
    public String getIndexView(Model model) {
        model.addAttribute("section", null);
        return "admin/index";
    }

    @GetMapping("/tanks")
    public String getTanksView(Model model, @ModelAttribute TanksSearchCriteria filter, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        List<TankDto> tanksList = this.tankService.findAllBySearchCriteria(filter);

        model.addAttribute("viewModel", TanksViewModel.builder()
                .tanks(tanksList)
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .filter(filter)
                .build());

        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setReplaceUrl(UrlUtils.fromObject(request.getRequestURI(), filter).build().encode().toUriString());
            return "/fragments/admin/tanks :: content";
        }

        model.addAttribute("section", "tanks");
        return "/admin/index";
    }
}
