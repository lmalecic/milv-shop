package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.ViewContext;
import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TanksSearchCriteria;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tanks")
@RequiredArgsConstructor
public class TanksMvcController {

    private final TankService tankService;
    private final NationService nationService;
    private final TankRoleService tankRoleService;

    @GetMapping({"", "/"})
    public String getTanksView(Model model, @ModelAttribute TanksSearchCriteria filter, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        List<TankDto> tanksList = this.tankService.findAllBySearchCriteria(filter);

        model.addAttribute("viewModel", TanksViewModel.builder()
                .tanks(tanksList)
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .filter(filter)
                .build());
        model.addAttribute("viewContext", ViewContext.VIEW);
        model.addAttribute("itemClickPath", requestUri);

        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setReplaceUrl(UrlUtils.fromObject(requestUri, filter).build().encode().toUriString());
            return "/fragments/tank/tanks-grid :: content";
        }

        return "/tank/tanks";
    }

    @GetMapping("/{id}")
    public String getTankView(Model model, @PathVariable Long id) {
        var tank = this.tankService.findById(id);
        if (tank.isEmpty()) {
            throw new ResourceNotFoundException("Tank with id " + id + " not found.");
        }
        model.addAttribute("tank", tank.get());
        return "/tank/tank";
    }
}
