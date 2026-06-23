package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.dto.*;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.service.NationService;
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
@RequestMapping("/admin/nations")
public class AdminNationsMvcController {

    private static final String INDEX_URI = "/admin/nations";
    private static final String REDIRECT_INDEX = "redirect:/admin/nations/";
    private static final String MODEL_FORM_FRAGMENT = "fragments/admin/nations/details-form";
    private static final String MODEL_LIST_FRAGMENT = "fragments/admin/nations/list";
    private static final String MODEL_CONFIRM_DELETE_FRAGMENT = "fragments/admin/confirm-delete";

    private final NationService nationService;

    @GetMapping({"", "/"})
    public String getIndex(Model model, @ModelAttribute("searchCriteria") NationSearchCriteria searchCriteria, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        this.buildListModel(model, searchCriteria, requestUri);

        String sectionUrl = UrlUtils.urlWithParams(requestUri, searchCriteria).toUriString();
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setPushUrl(sectionUrl);
            return "/fragments/admin/nations/index";
        }

        model.addAttribute("sectionUrl", sectionUrl);
        return AdminMvcController.INDEX_VIEW;
    }

    @HxRequest
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("searchCriteria") NationSearchCriteria searchCriteria, HtmxResponse htmxResponse) {
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
    public String create(Model model, @Valid @ModelAttribute NationDto nationDto, BindingResult bindingResult, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.CREATE);
            return MODEL_FORM_FRAGMENT;
        }

        var created = this.nationService.create(nationDto);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Nation updated successfully."));
            this.buildDetailsModel(model, created);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + created.id();
    }

    @HxRequest
    @GetMapping("/delete/{id}")
    public String getDeleteForm(Model model, @PathVariable Long id) {
        model.addAttribute("targetObject", this.nationService.findById(id)
                        .map(Displayable.class::cast)
                .orElseThrow(() -> new ResourceNotFoundException("Nation with id " + id + " not found.")));
        model.addAttribute("formAction", "/admin/nations/delete/" + id);
        return MODEL_CONFIRM_DELETE_FRAGMENT;
    }

    @DeleteMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Long id, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        var deleted = this.nationService.deleteById(id);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Nation deleted successfully."));
            this.buildDetailsModel(model, deleted);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + deleted.id();
    }

    @PatchMapping("/recover/{id}")
    public String recover(Model model, @PathVariable Long id, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        var recovered = this.nationService.recoverById(id);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Nation recovered successfully."));
            this.buildDetailsModel(model, recovered);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + recovered.id();
    }

    @PatchMapping("/edit")
    public String edit(Model model, @Valid @ModelAttribute NationDto nationDto, BindingResult bindingResult, HtmxRequest htmxRequest, HtmxResponse htmxResponse) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
            return MODEL_FORM_FRAGMENT;
        }

        var updated = this.nationService.update(nationDto);
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.addTrigger(Constants.REFRESH_LIST_EVENT);
            htmxResponse.addTrigger(Constants.PUSH_TOAST_EVENT, Toast.success("Nation updated successfully."));
            this.buildDetailsModel(model, updated);
            return MODEL_FORM_FRAGMENT;
        }
        return REDIRECT_INDEX + updated.id();
    }

    private void buildListModel(Model model, NationSearchCriteria searchCriteria, String requestUri) {
        model.addAttribute("results", this.nationService.findAllBySearchCriteria(searchCriteria));
        model.addAttribute("itemClickPath", requestUri);
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
    }

    private void buildCreateModel(Model model) {
        this.buildDetailsModel(model, NationDto.empty());
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.CREATE);
    }

    private void buildDetailsModel(Model model, Long nationId) {
        this.buildDetailsModel(model, this.nationService.findById(nationId)
                .orElseThrow(() -> new ResourceNotFoundException("Nation with id " + nationId + " not found.")));
    }

    private void buildDetailsModel(Model model, NationDto nationDto) {
        model.addAttribute("nationDto", nationDto);
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
    }
}
