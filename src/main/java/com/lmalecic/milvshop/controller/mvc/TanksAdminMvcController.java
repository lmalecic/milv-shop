package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.service.NationService;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.service.TankService;
import com.lmalecic.milvshop.viewmodel.TankCreateViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tanks")
@RequiredArgsConstructor
public class TanksAdminMvcController {

    private final TankService tankService;
    private final NationService nationService;
    private final TankRoleService tankRoleService;

    @GetMapping("/create")
    public String getCreateView(Model model) {
        model.addAttribute("viewModel", TankCreateViewModel.builder()
                .tank(new Tank())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .build());
        return "/admin/tank/create";
    }

    @PostMapping("/create")
    public String createTank(@ModelAttribute Tank tank) {
        Tank newTank = this.tankService.create(tank);
        return "redirect:/tanks/" + newTank.getId();
    }

    @GetMapping("/delete/{id}")
    public String getDeleteView(Model model, @PathVariable Long id) {
        return "/admin/tank/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteTank(@PathVariable Long id) {
        // TODO: Redirect to previous page?
        this.tankService.deleteById(id);
        return "redirect:/tanks";
    }

    @GetMapping("/edit/{id}")
    public String getEditView(Model model, @PathVariable Long id) {
        model.addAttribute("viewModel", TankCreateViewModel.builder()
                .tank(this.tankService.findById(id).orElseThrow(() -> new IllegalArgumentException("Tank with id " + id + " not found.")))
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .build());
        return "/admin/tank/edit";
    }

    @PutMapping("/edit/{id}")
    public String editTank(@PathVariable Long id, @ModelAttribute Tank tank) {
        var updated = this.tankService.update(id, tank);
        return "redirect:/tanks/" + updated.getId();
    }
}
