package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.dto.TanksFilterModel;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.service.NationService;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.service.TankService;
import com.lmalecic.milvshop.util.UrlUtils;
import com.lmalecic.milvshop.viewmodel.TanksViewModel;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
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
    public String getTanksView(Model model, @ModelAttribute TanksFilterModel filter, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        List<Tank> tanksList = filter.hasActiveFilters() ? this.tankService.findAllFiltered(filter) : this.tankService.findAll();

        model.addAttribute("viewModel", TanksViewModel.builder()
                .tanks(tanksList)
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .filter(filter)
                .build());

        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setReplaceUrl(UrlUtils.fromObject("/tanks", filter).build().encode().toUriString());
        }

        return htmxRequest.isHtmxRequest()
                ? "/fragments/tank/tanks :: tanksGrid"
                : "/tank/tanks";
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
