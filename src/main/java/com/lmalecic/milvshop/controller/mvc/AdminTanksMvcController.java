package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.viewmodel.Toast;
import com.lmalecic.milvshop.viewmodel.ViewContext;
import com.lmalecic.milvshop.dto.Displayable;
import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TankSearchCriteria;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.service.NationService;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.service.TankService;
import com.lmalecic.milvshop.util.UrlUtils;
import com.lmalecic.milvshop.dto.TankSearchResults;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/tanks")
public class AdminTanksMvcController {

    private final TankService tankService;
    private final NationService nationService;
    private final TankRoleService tankRoleService;

    @GetMapping({"", "/"})
    public String getTanksView(Model model, @ModelAttribute TankSearchCriteria filter, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        this.buildListModel(model, filter, requestUri);

        String sectionUrl = UrlUtils.urlWithParams(requestUri, filter).toUriString();

        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setPushUrl(sectionUrl);
            return "/fragments/admin/tanks/index";
        }

        model.addAttribute("sectionUrl", sectionUrl);
        return "admin";
    }

    @HxRequest
    @GetMapping("/search")
    public String searchTanks(Model model, @ModelAttribute TankSearchCriteria filter, HtmxResponse htmxResponse) {
        String requestUri = "/admin/tanks";
        this.buildListModel(model, filter, requestUri);

        htmxResponse.setPushUrl(UrlUtils.urlWithParams(requestUri, filter).toUriString());

        return "/fragments/tank/tanks-grid";
    }

    @HxRequest
    @GetMapping("/{id}")
    public String getTankForm(Model model, @PathVariable Long id) {
        this.buildDetailsModel(model, id);
        return "fragments/admin/tanks/tank-form";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        this.buildDetailsModel(model);
        return "fragments/admin/tanks/tank-form";
    }

    @PostMapping("/create")
    public String createTank(@ModelAttribute TankDto tankDto) {
        TankDto newTank = this.tankService.create(tankDto);
        return "redirect:/admin/tanks/" + newTank.getId();
    }

    @HxRequest
    @GetMapping("/delete/{id}")
    public String getDeleteForm(Model model, @PathVariable Long id) {
        model.addAttribute("targetObject", this.tankService.findById(id)
                        .map(Displayable.class::cast)
                .orElseThrow(() -> new ResourceNotFoundException("Tank with id " + id + " not found.")));
        model.addAttribute("formAction", "/admin/tanks/delete/" + id);
        return "fragments/admin/confirm-delete";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTank(Model model, @PathVariable Long id, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        TankDto tank = this.tankService.deleteById(id);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger("refreshList");
            htmxResponse.addTrigger("pushToast", Toast.success("Tank deleted successfully."));
            this.buildDetailsModel(model, tank);
            return "fragments/admin/tanks/tank-form";
        }
        return "redirect:/admin/tanks/" + tank.getId();
    }

    @PatchMapping("/recover/{id}")
    public String recoverTank(Model model, @PathVariable Long id, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        TankDto tank = this.tankService.recoverById(id);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger("refreshList");
            htmxResponse.addTrigger("pushToast", Toast.success("Tank recovered successfully."));
            this.buildDetailsModel(model, tank);
            return "fragments/admin/tanks/tank-form";
        }
        return "redirect:/admin/tanks/" + tank.getId();
    }

    @PatchMapping("/edit")
    public String editTank(@ModelAttribute TankDto tank) {
        TankDto updated = this.tankService.update(tank);
        return "redirect:/tanks/" + updated.getId();
    }

    private void buildListModel(Model model, @ModelAttribute TankSearchCriteria filter, String requestUri) {
        model.addAttribute("results", TankSearchResults.builder()
                .tanks(this.tankService.findAllBySearchCriteria(filter))
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllOrdered())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .filter(filter)
                .build());
        model.addAttribute("viewContext", ViewContext.ADMIN);
        model.addAttribute("itemClickPath", requestUri);
    }

    private void buildDetailsModel(Model model) {
        model.addAttribute("tank", new TankDto());
        model.addAttribute("nations", this.nationService.findAllOrdered());
        model.addAttribute("tankRoles", this.tankRoleService.findAllOrdered());
        model.addAttribute("viewContext", ViewContext.CREATE);
    }

    private void buildDetailsModel(Model model, Long tankId) {
        TankDto tank = tankId != null
                ? this.tankService.findById(tankId).orElseThrow(() -> new ResourceNotFoundException("Tank with id " + tankId + " not found."))
                : new TankDto();

        model.addAttribute("tank", tank);
        model.addAttribute("nations", this.nationService.findAllOrdered());
        model.addAttribute("tankRoles", this.tankRoleService.findAllOrdered());
        model.addAttribute("viewContext", ViewContext.ADMIN);
    }

    private void buildDetailsModel(Model model, TankDto tank) {
        model.addAttribute("tank", tank);
        model.addAttribute("nations", this.nationService.findAllOrdered());
        model.addAttribute("tankRoles", this.tankRoleService.findAllOrdered());
        model.addAttribute("viewContext", ViewContext.ADMIN);
    }
}
