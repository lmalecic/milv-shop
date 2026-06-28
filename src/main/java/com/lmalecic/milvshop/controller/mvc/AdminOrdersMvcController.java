package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.criteria.OrderSearchCriteria;

import com.lmalecic.milvshop.dto.OrderDto;
import com.lmalecic.milvshop.entity.OrderStatus;
import com.lmalecic.milvshop.service.OrderService;
import com.lmalecic.milvshop.util.UrlUtils;
import com.lmalecic.milvshop.viewmodel.Toast;
import com.lmalecic.milvshop.viewmodel.ViewContext;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/orders")
public class AdminOrdersMvcController {

    private static final String INDEX_URI = "/admin/orders";
    private static final String MODEL_LIST_FRAGMENT = "fragments/orders/list";
    private static final String ITEM_FRAGMENT = "fragments/orders/item";

    private static final String ORDER_ATTRIBUTE = "order";

    private final OrderService orderService;

    @GetMapping
    public String getIndex(Model model, @ModelAttribute("searchCriteria") OrderSearchCriteria criteria, HtmxRequest htmxRequest, HtmxResponse htmxResponse, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String sectionUrl = UrlUtils.urlWithParams(requestUri, criteria).toUriString();

        model.addAttribute("orders", this.orderService.findAllOrdersByCriteria(criteria));
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);

        if (htmxRequest.isHtmxRequest()) {
            htmxResponse.setPushUrl(sectionUrl);
            return "fragments/admin/orders/index";
        }

        model.addAttribute("sectionUrl", sectionUrl);
        return AdminMvcController.INDEX_VIEW;
    }

    @HxRequest
    @GetMapping("search")
    public String search(Model model, @ModelAttribute("searchCriteria") OrderSearchCriteria criteria, HtmxResponse htmxResponse) {
        model.addAttribute("orders", this.orderService.findAllOrdersByCriteria(criteria));
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
        htmxResponse.setPushUrl(UrlUtils.urlWithParams(INDEX_URI, criteria).toUriString());
        return MODEL_LIST_FRAGMENT;
    }

    @HxRequest
    @PatchMapping("{id}/complete")
    public String completeOrder(Model model, @PathVariable Long id, HtmxResponse htmxResponse) {
        OrderDto order = this.orderService.setStatus(id, OrderStatus.COMPLETED);
        model.addAttribute(ORDER_ATTRIBUTE, order);
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
        htmxResponse.addTrigger(Toast.PUSH_TOAST_EVENT, Toast.success("Order completed successfully"));
        return ITEM_FRAGMENT;
    }

    @HxRequest
    @PatchMapping("{id}/cancel")
    public String cancelOrder(Model model, @PathVariable Long id, HtmxResponse htmxResponse) {
        OrderDto order = this.orderService.setStatus(id, OrderStatus.CANCELLED);
        model.addAttribute(ORDER_ATTRIBUTE, order);
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
        htmxResponse.addTrigger(Toast.PUSH_TOAST_EVENT, Toast.success("Order cancelled successfully"));
        return ITEM_FRAGMENT;
    }

    @HxRequest
    @PatchMapping("{id}/undo")
    public String undoOrder(Model model, @PathVariable Long id, HtmxResponse htmxResponse) {
        OrderDto order = this.orderService.setStatus(id, OrderStatus.PENDING);
        model.addAttribute(ORDER_ATTRIBUTE, order);
        model.addAttribute(ViewContext.MODEL_ATTRIBUTE_NAME, ViewContext.ADMIN);
        htmxResponse.addTrigger(Toast.PUSH_TOAST_EVENT, Toast.success("Order undone successfully"));
        return ITEM_FRAGMENT;
    }
}
