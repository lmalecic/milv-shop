package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.criteria.OrderSearchCriteria;
import com.lmalecic.milvshop.dto.OrderDto;
import com.lmalecic.milvshop.entity.Order;
import com.lmalecic.milvshop.entity.User;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.service.OrderService;
import com.lmalecic.milvshop.viewmodel.ViewContext;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrdersMvcController {

    private final OrderService orderService;

    @GetMapping
    public String getOrdersView(Model model, @AuthenticationPrincipal User user, @ModelAttribute("searchCriteria") OrderSearchCriteria criteria, HtmxRequest htmxRequest) {
        model.addAttribute("orders", this.orderService.findAllUserOrdersByCriteria(user, criteria));
        model.addAttribute("viewContext", ViewContext.VIEW);

        if (htmxRequest.isHtmxRequest()) {
            return "fragments/orders/list";
        }

        return "orders";
    }

    @GetMapping("{id}")
    public String getOrder(Model model, @AuthenticationPrincipal User user, @PathVariable Long id, HtmxRequest htmxRequest) {
        OrderDto order = this.orderService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found"));

        if (user.getId().equals(order.user().id())) {
            log.warn("User {} attempted to access order {} that does not belong to them", user.getId(), id);
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }

        model.addAttribute("order", order);
        model.addAttribute("viewContext", ViewContext.VIEW);

        if (htmxRequest.isHtmxRequest()) {
            return "fragments/orders/details";
        }

        return "orders";
    }
}
