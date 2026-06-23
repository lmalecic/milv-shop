package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.util.Constants;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/tanks")
public class AdminTanksMvcController {

    private static final String INDEX_URI = "/admin/tanks";
    private static final String REDIRECT_INDEX = "redirect:/admin/tanks/";
    private static final String MODEL_FORM_FRAGMENT = "fragments/admin/tanks/details-form";
    private static final String MODEL_LIST_FRAGMENT = "fragments/tanks/list";
    private static final String MODEL_CONFIRM_DELETE_FRAGMENT = "fragments/admin/confirm-delete";

    private final TankService tankService;
    private final NationService nationService;
    private final TankRoleService tankRoleService;

    @GetMapping({"", "/"})
    public String getIndex(Model model, @ModelAttribute("searchCriteria") TankSearchCriteria searchCriteria, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        this.buildListModel(model, searchCriteria, requestUri);

        String sectionUrl = UrlUtils.urlWithParams(requestUri, searchCriteria).toUriString();
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setPushUrl(sectionUrl);
            return "/fragments/admin/tanks/index";
        }

        model.addAttribute("sectionUrl", sectionUrl);
        return AdminMvcController.INDEX_VIEW;
    }

    @HxRequest
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("searchCriteria") TankSearchCriteria searchCriteria, HtmxResponse htmxResponse) {
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
    public String create(Model model, @Valid @ModelAttribute TankDto tankDto, BindingResult bindingResult, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        if (bindingResult.hasErrors()) {
            this.buildFormOptionsModel(model, ViewContext.CREATE);
            return MODEL_FORM_FRAGMENT;
        }

        var created = this.tankService.create(tankDto);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Tank updated successfully."));
            this.buildDetailsModel(model, created);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + created.id();
    }

    @HxRequest
    @GetMapping("/delete/{id}")
    public String getDeleteForm(Model model, @PathVariable Long id) {
        model.addAttribute("targetObject", this.tankService.findById(id)
                        .map(Displayable.class::cast)
                .orElseThrow(() -> new ResourceNotFoundException("Tank with id " + id + " not found.")));
        model.addAttribute("formAction", "/admin/tanks/delete/" + id);
        return MODEL_CONFIRM_DELETE_FRAGMENT;
    }

    @DeleteMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Long id, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        var deleted = this.tankService.deleteById(id);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Tank deleted successfully."));
            this.buildDetailsModel(model, deleted);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + deleted.id();
    }

    @PatchMapping("/recover/{id}")
    public String recover(Model model, @PathVariable Long id, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        var recovered = this.tankService.recoverById(id);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Tank recovered successfully."));
            this.buildDetailsModel(model, recovered);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + recovered.id();
    }

    @PatchMapping("/edit")
    public String edit(Model model, @Valid @ModelAttribute TankDto tankDto, BindingResult bindingResult, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        if (bindingResult.hasErrors()) {
            this.buildFormOptionsModel(model, ViewContext.ADMIN);
            return MODEL_FORM_FRAGMENT;
        }

        TankDto updated = this.tankService.update(tankDto);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Tank updated successfully."));
            this.buildDetailsModel(model, updated);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + updated.id();
    }

    private void buildListModel(Model model, TankSearchCriteria criteria, String requestUri) {
        model.addAttribute("results", TankSearchResults.builder()
                .tanks(this.tankService.findAllBySearchCriteria(criteria))
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllActive())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .build());
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
        model.addAttribute("itemClickPath", requestUri);
    }

    private void buildCreateModel(Model model) {
        this.buildDetailsModel(model, TankDto.builder().build());
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.CREATE);
    }

    private void buildDetailsModel(Model model, Long tankId) {
        this.buildDetailsModel(model, this.tankService.findById(tankId)
                .orElseThrow(() -> new ResourceNotFoundException("Tank with id " + tankId + " not found.")));
    }

    private void buildDetailsModel(Model model, TankDto tank) {
        model.addAttribute("tankDto", tank);
        this.buildFormOptionsModel(model, ViewContext.ADMIN);
    }

    private void buildFormOptionsModel(Model model, ViewContext viewContext) {
        model.addAttribute("nations", this.nationService.findAllActive());
        model.addAttribute("tankRoles", this.tankRoleService.findAllOrdered());
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, viewContext);
    }
}
