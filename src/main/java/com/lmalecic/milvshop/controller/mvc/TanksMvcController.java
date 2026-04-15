package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.dto.TanksFilterModel;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.service.NationService;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.service.TankService;
import com.lmalecic.milvshop.viewmodel.TankCreateViewModel;
import com.lmalecic.milvshop.viewmodel.TanksViewModel;
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
    public String getTanksView(Model model) {
        model.addAttribute("viewModel", TanksViewModel.builder()
                .tanks(this.tankService.findAll())
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .filter(new TanksFilterModel())
                .build());
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

    @GetMapping("/filter")
    public String getTanksViewFilter(Model model, @ModelAttribute TanksFilterModel filter) {
        List<Tank> tanksList;

        if (hasActiveFilters(filter)) {
            tanksList = this.tankService.findAllFiltered(filter);
        } else {
            tanksList = this.tankService.findAll();
            filter = new TanksFilterModel();
        }

        model.addAttribute("viewModel", TanksViewModel.builder()
                .tanks(tanksList)
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .filter(filter)
                .build());
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
