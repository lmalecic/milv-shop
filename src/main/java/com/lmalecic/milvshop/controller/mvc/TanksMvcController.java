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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<Tank> tanksList;
        TanksFilterModel filter = (TanksFilterModel) model.getAttribute("filter");
        System.out.println("Filter model: " + filter);

        if (filter != null && hasActiveFilters(filter)) {
            tanksList = this.tankService.findAllFiltered(filter);
        } else {
            tanksList = this.tankService.findAll();
            filter = new TanksFilterModel();
        }

        model.addAttribute("tanksList", tanksList);
        model.addAttribute("nationsList", this.nationService.findAll());
        model.addAttribute("tankRolesList", this.tankRoleService.findAll());
        model.addAttribute("filterModel", filter);
        return "tanks";
    }

    @PostMapping("/filter")
    public String getTanksViewFilter(@ModelAttribute TanksFilterModel filter, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("Received filter: " + filter);
        List<Tank> tanksList;
//        redirectAttributes.addFlashAttribute("filter", filter);
        if (filter != null && hasActiveFilters(filter)) {
            tanksList = this.tankService.findAllFiltered(filter);
        } else {
            tanksList = this.tankService.findAll();
            filter = new TanksFilterModel();
        }

        model.addAttribute("tanksList", tanksList);
        model.addAttribute("nationsList", this.nationService.findAll());
        model.addAttribute("tankRolesList", this.tankRoleService.findAll());
        model.addAttribute("filterModel", filter);

        return "tanks";
    }

    private boolean hasActiveFilters(TanksFilterModel filter) {
        return (filter.getSearchQuery() != null && !filter.getSearchQuery().isEmpty()) ||
                (filter.getNationIds() != null && !filter.getNationIds().isEmpty()) ||
                (filter.getTankRoleIds() != null && !filter.getTankRoleIds().isEmpty());
    }
}
