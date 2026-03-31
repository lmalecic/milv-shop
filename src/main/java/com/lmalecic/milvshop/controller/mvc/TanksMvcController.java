package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.dto.TanksFilterModel;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.service.NationService;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.service.TankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tanks")
@RequiredArgsConstructor
public class TanksMvcController {

    private final TankService tankService;
    private final NationService nationService;
    private final TankRoleService tankRoleService;

    @GetMapping("")
    public String getTanksView(Model model) {
        model.addAttribute("tanksList", this.tankService.findAll());
        model.addAttribute("gunCalibresList", this.tankService.findAllMainGunCalibres());
        model.addAttribute("nationsList", this.nationService.findAllOrdered());
        model.addAttribute("tankRolesList", this.tankRoleService.findAll());
        model.addAttribute("filterModel", new TanksFilterModel());
        return "tank/tanks";
    }

    @GetMapping("{id}")
    public String getTankView(@PathVariable Long id, Model model) {
        var tank = this.tankService.findById(id);
        if (tank.isEmpty()) {
            return "/error/404";
        }
        model.addAttribute("tank", tank.get());
        return "/tank/tank";
    }

    @GetMapping("/filter")
    public String getTanksViewFilter(@ModelAttribute TanksFilterModel filter, Model model) {
        System.out.println("Received filter: " + filter);
        System.out.println("Has active filters: " + hasActiveFilters(filter));
        List<Tank> tanksList;

        if (hasActiveFilters(filter)) {
            tanksList = this.tankService.findAllFiltered(filter);
        } else {
            tanksList = this.tankService.findAll();
            filter = new TanksFilterModel();
        }

        model.addAttribute("tanksList", tanksList);
        model.addAttribute("gunCalibresList", this.tankService.findAllMainGunCalibres());
        model.addAttribute("nationsList", this.nationService.findAll());
        model.addAttribute("tankRolesList", this.tankRoleService.findAll());
        model.addAttribute("filterModel", filter);

        return "/tank/tanks";
    }

    private boolean hasActiveFilters(TanksFilterModel filter) {
        return (filter.getSearchQuery() != null && !filter.getSearchQuery().isEmpty()) ||
                (filter.getNationIds() != null && !filter.getNationIds().isEmpty()) ||
                (filter.getTankRoleIds() != null && !filter.getTankRoleIds().isEmpty()) ||
                (filter.getPriceMin() != null) || (filter.getPriceMax() != null) ||
                (filter.getMainGunCalibre() != null) ||
                (filter.getArmorThicknessMin() != null) || (filter.getArmorThicknessMax() != null) ||
                (filter.getMaxSpeed() != null) ||
                (filter.getCrewSize() != null);
    }
}
