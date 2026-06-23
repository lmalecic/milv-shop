package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.criteria.TankRoleSearchCriteria;
import com.lmalecic.milvshop.dto.Displayable;
import com.lmalecic.milvshop.dto.TankRoleDto;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.util.Constants;
import com.lmalecic.milvshop.util.UrlUtils;
import com.lmalecic.milvshop.viewmodel.Toast;
import com.lmalecic.milvshop.viewmodel.ViewContext;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/tank-roles")
public class AdminTankRolesMvcController {

    private static final String INDEX_URI = "/admin/tank-roles";
    private static final String REDIRECT_INDEX = "redirect:/admin/tank-roles/";
    private static final String MODEL_FORM_FRAGMENT = "fragments/admin/tank-roles/details-form";
    private static final String MODEL_LIST_FRAGMENT = "fragments/admin/tank-roles/list";
    private static final String MODEL_CONFIRM_DELETE_FRAGMENT = "fragments/admin/confirm-delete";

    private final TankRoleService tankRoleService;

    @GetMapping({"", "/"})
    public String getIndex(Model model, @ModelAttribute("searchCriteria") TankRoleSearchCriteria searchCriteria, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        this.buildListModel(model, searchCriteria, requestUri);

        String sectionUrl = UrlUtils.urlWithParams(requestUri, searchCriteria).toUriString();
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setPushUrl(sectionUrl);
            return "/fragments/admin/tank-roles/index";
        }

        model.addAttribute("sectionUrl", sectionUrl);
        return AdminMvcController.INDEX_VIEW;
    }

    @HxRequest
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("searchCriteria") TankRoleSearchCriteria searchCriteria, HtmxResponse htmxResponse) {
        this.buildListModel(model, searchCriteria, INDEX_URI);
        htmxResponse.setPushUrl(UrlUtils.urlWithParams(INDEX_URI, searchCriteria).toUriString());
        return MODEL_LIST_FRAGMENT;
    }

    @HxRequest
    @GetMapping("/{id}")
    public String getDetailsForm(Model model, @PathVariable Long id) {
        this.buildDetailsModel(model, id);
        return MODEL_FORM_FRAGMENT;
    }

    @HxRequest
    @GetMapping("/create")
    public String getCreateForm(Model model) {
        this.buildCreateModel(model);
        return MODEL_FORM_FRAGMENT;
    }

    @PostMapping("/create")
    public String create(Model model, @Valid @ModelAttribute TankRoleDto tankRoleDto, BindingResult bindingResult, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.CREATE);
            return MODEL_FORM_FRAGMENT;
        }

        var created = this.tankRoleService.create(tankRoleDto);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Tank Role updated successfully."));
            this.buildDetailsModel(model, created);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + created.id();
    }

    @HxRequest
    @GetMapping("/delete/{id}")
    public String getDeleteForm(Model model, @PathVariable Long id) {
        model.addAttribute("targetObject", this.tankRoleService.findById(id)
                        .map(Displayable.class::cast)
                .orElseThrow(() -> new ResourceNotFoundException("tankRole with id " + id + " not found.")));
        model.addAttribute("formAction", "/admin/tank-roles/delete/" + id);
        return MODEL_CONFIRM_DELETE_FRAGMENT;
    }

    @DeleteMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Long id, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        var deleted = this.tankRoleService.deleteById(id);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Tank Role deleted successfully."));
            this.buildDetailsModel(model, deleted);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + deleted.id();
    }

    @PatchMapping("/recover/{id}")
    public String recover(Model model, @PathVariable Long id, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        var recovered = this.tankRoleService.recoverById(id);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Tank Role recovered successfully."));
            this.buildDetailsModel(model, recovered);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + recovered.id();
    }

    @PatchMapping("/edit")
    public String edit(Model model, @Valid @ModelAttribute TankRoleDto tankRoleDto, BindingResult bindingResult, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
            return MODEL_FORM_FRAGMENT;
        }

        var updated = this.tankRoleService.update(tankRoleDto);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Tank Role updated successfully."));
            this.buildDetailsModel(model, updated);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + updated.id();
    }

    private void buildListModel(Model model, TankRoleSearchCriteria searchCriteria, String requestUri) {
        model.addAttribute("results", this.tankRoleService.findAllBySearchCriteria(searchCriteria));
        model.addAttribute("itemClickPath", requestUri);
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
    }

    private void buildCreateModel(Model model) {
        this.buildDetailsModel(model, TankRoleDto.empty());
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.CREATE);
    }

    private void buildDetailsModel(Model model, Long tankRoleId) {
        this.buildDetailsModel(model, this.tankRoleService.findById(tankRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Tank Role with id " + tankRoleId + " not found.")));
    }

    private void buildDetailsModel(Model model, TankRoleDto tankRoleDto) {
        model.addAttribute("tankRoleDto", tankRoleDto);
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
    }
}
