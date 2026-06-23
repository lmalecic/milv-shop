package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.viewmodel.ViewContext;
import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TankSearchCriteria;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.service.NationService;
import com.lmalecic.milvshop.service.TankRoleService;
import com.lmalecic.milvshop.service.TankService;
import com.lmalecic.milvshop.util.UrlUtils;
import com.lmalecic.milvshop.dto.TankSearchResults;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
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
    public String getIndex(Model model, @ModelAttribute("searchCriteria") TankSearchCriteria searchCriteria, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        List<TankDto> tanksList = this.tankService.findAllBySearchCriteria(searchCriteria);

        model.addAttribute("results", TankSearchResults.builder()
                .tanks(tanksList)
                .mainGunCalibres(this.tankService.findAllMainGunCalibres())
                .nations(this.nationService.findAllActive())
                .tankRoles(this.tankRoleService.findAllOrdered())
                .build());
        model.addAttribute("viewContext", ViewContext.VIEW);
        model.addAttribute("itemClickPath", requestUri);

        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setReplaceUrl(UrlUtils.urlWithParams(requestUri, searchCriteria).toUriString());
            return "fragments/tanks/list";
        }

        return "/tank/tanks";
    }

    @HxRequest
    @GetMapping("/{id}")
    public String getDetailsForm(Model model, @PathVariable Long id) {
        TankDto tank = this.tankService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tank with id " + id + " not found."));

        model.addAttribute("tank", tank);
        model.addAttribute("viewContext", ViewContext.VIEW);

        return "fragments/tanks/details-form";
    }
}
