package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.ViewContext;
import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TanksSearchCriteria;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.service.NationService;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.service.TankService;
import com.lmalecic.milvshop.util.UrlUtils;
import com.lmalecic.milvshop.viewmodel.TankViewModel;
import com.lmalecic.milvshop.viewmodel.TanksViewModel;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/tanks")
public class AdminTanksMvcController {

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
        model.addAttribute("viewContext", ViewContext.ADMIN);
        model.addAttribute("itemClickPath", requestUri);

        String sectionUrl = UrlUtils.urlWithParams(requestUri, filter).toUriString();

        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setPushUrl(sectionUrl);
            return "/fragments/admin/tanks/index";
        }

        model.addAttribute("sectionUrl", sectionUrl);
        return "/admin/index";
    }

    @HxRequest
    @GetMapping("/search")
    public String searchTanks(Model model, @ModelAttribute TanksSearchCriteria filter, HtmxResponse htmxResponse) {
        String requestUri = "/admin/tanks";
        List<TankDto> tanksList = this.tankService.findAllBySearchCriteria(filter);

        model.addAttribute("viewModel", TanksViewModel.builder()
                .tanks(tanksList)
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .filter(filter)
                .build());
        model.addAttribute("viewContext", ViewContext.ADMIN);
        model.addAttribute("itemClickPath", requestUri);

        htmxResponse.setPushUrl(UrlUtils.urlWithParams(requestUri, filter).toUriString());

        return "/fragments/tank/tanks-grid";
    }

    @HxRequest
    @GetMapping("/{id}")
    public String getTankForm(Model model, @PathVariable Long id) {
        Optional<TankDto> tank = this.tankService.findById(id);
        if (tank.isEmpty()) {
            throw new ResourceNotFoundException("Tank with id " + id + " not found.");
        }

        model.addAttribute("viewModel", TankViewModel.builder()
                .tank(tank.get())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .build());
        model.addAttribute("viewContext", ViewContext.ADMIN);

        return "fragments/admin/tanks/tank-form";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("viewModel", TankViewModel.builder()
                .tank(new TankDto())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .build());
        model.addAttribute("viewContext", ViewContext.CREATE);
        return "fragments/admin/tanks/tank-form";
    }

    @PostMapping("/create")
    public String createTank(@ModelAttribute TankDto tankDto) {
        TankDto newTank = this.tankService.create(tankDto);
        return "redirect:/tanks/" + newTank.getId();
    }

    @HxRequest
    @GetMapping("/delete/{id}")
    public String getDeleteView(Model model, @PathVariable Long id) {
        return "fragments/admin/tanks/delete-confirmation";
    }

    @PostMapping("/delete/{id}")
    public String deleteTank(@PathVariable Long id) {
        this.tankService.deleteById(id);
        return "redirect:/admin/tanks";
    }

    @GetMapping("/edit/{id}")
    public String getEditView(Model model, @PathVariable Long id) {
        model.addAttribute("viewModel", TankViewModel.builder()
                .tank(this.tankService.findById(id).orElseThrow(() -> new IllegalArgumentException("Tank with id " + id + " not found.")))
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .build());
        return "/admin/tank/edit";
    }

    @PutMapping("/edit/{id}")
    public String editTank(@PathVariable Long id, @ModelAttribute TankDto tank) {
        var updated = this.tankService.update(id, tank);
        return "redirect:/tanks/" + updated.getId();
    }
}
