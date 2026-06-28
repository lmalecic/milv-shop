package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.criteria.OrderSearchCriteria;
import com.lmalecic.milvshop.dto.OrderDto;
import com.lmalecic.milvshop.entity.Order;
import com.lmalecic.milvshop.entity.Order_;
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

    private static final String ENTITY_NOT_FOUND_MESSAGE = "Order with %s %s not found!";
    private static final String MODEL_ATTRIBUTE_ORDERS = "orders";
    private static final String INDEX_VIEW = "orders";
    private final OrderService orderService;

    @GetMapping
    public String getOrdersView(Model model, @AuthenticationPrincipal User user, @ModelAttribute("searchCriteria") OrderSearchCriteria criteria, HtmxRequest htmxRequest) {
        model.addAttribute(MODEL_ATTRIBUTE_ORDERS, this.orderService.findAllUserOrdersByCriteria(user, criteria));
        model.addAttribute("viewContext", ViewContext.VIEW);

        if (htmxRequest.isHtmxRequest()) {
            return "fragments/orders/list";
        }

        return INDEX_VIEW;
    }

    @GetMapping("{id}")
    public String getOrder(Model model, @AuthenticationPrincipal User user, @PathVariable Long id, HtmxRequest htmxRequest) {
        OrderDto order = this.orderService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(Order_.ID, id)));

        if (user.getId().equals(order.user().id())) {
            log.warn("User {} attempted to access order {} that does not belong to them", user.getId(), id);
            throw new ResourceNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(Order_.ID, id));
        }

        model.addAttribute("order", order);
        model.addAttribute("viewContext", ViewContext.VIEW);

        if (htmxRequest.isHtmxRequest()) {
            return "fragments/orders/details";
        }

        return INDEX_VIEW;
    }
}
