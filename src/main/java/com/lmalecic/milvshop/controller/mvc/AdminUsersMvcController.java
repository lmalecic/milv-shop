package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.criteria.TankSearchCriteria;
import com.lmalecic.milvshop.criteria.UserSearchCriteria;
import com.lmalecic.milvshop.dto.UserRoleDto;
import com.lmalecic.milvshop.results.UserSearchResults;
import com.lmalecic.milvshop.service.UserRoleService;
import com.lmalecic.milvshop.service.UserService;
import com.lmalecic.milvshop.util.UrlUtils;
import com.lmalecic.milvshop.viewmodel.ViewContext;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUsersMvcController {

    private static final String INDEX_URI = "/admin/users";
    private static final String MODEL_LIST_FRAGMENT = "/fragments/admin/users/list";

    private final UserService userService;
    private final UserRoleService userRoleService;

    @GetMapping({"", "/"})
    public String getIndexView(Model model, @ModelAttribute("searchCriteria") UserSearchCriteria searchCriteria, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        this.buildListModel(model, searchCriteria, requestUri);

        String sectionUrl = UrlUtils.urlWithParams(requestUri, searchCriteria).toUriString();
        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setPushUrl(sectionUrl);
            return "/fragments/admin/users/index";
        }

        model.addAttribute("sectionUrl", sectionUrl);
        return AdminMvcController.INDEX_VIEW;
    }

    @HxRequest
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("searchCriteria") UserSearchCriteria searchCriteria, HtmxResponse htmxResponse) {
        this.buildListModel(model, searchCriteria, INDEX_URI);
        htmxResponse.setPushUrl(UrlUtils.urlWithParams(INDEX_URI, searchCriteria).toUriString());
        return MODEL_LIST_FRAGMENT;
    }

    private void buildListModel(Model model, UserSearchCriteria searchCriteria, String requestUri) {
        model.addAttribute("results", UserSearchResults.builder()
                .users(this.userService.findAllBySearchCriteria(searchCriteria))
                .roles(this.userRoleService.findAllActive())
                .build());
        model.addAttribute("roleIds", this.userRoleService.findAllActive()
                .stream()
                .map(UserRoleDto::id)
                .toList());
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
        model.addAttribute("itemClickPath", requestUri);
    }
}
