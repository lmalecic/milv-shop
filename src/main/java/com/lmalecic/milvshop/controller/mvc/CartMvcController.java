package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.cart.Cart;
import com.lmalecic.milvshop.cart.PurchasableType;
import com.lmalecic.milvshop.cart.resolver.PurchasableResolverRegistry;
import com.lmalecic.milvshop.dto.CartItemDto;
import com.lmalecic.milvshop.util.Constants;
import com.lmalecic.milvshop.viewmodel.Toast;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Validated
@Controller
@RequiredArgsConstructor
@SessionAttributes("cart")
@RequestMapping("cart")
public class CartMvcController {

    private final PurchasableResolverRegistry purchasableResolverRegistry;

    @ModelAttribute("cart")
    public Cart createCart() {
        return new Cart();
    }

    @GetMapping
    public String getIndex(Model model, HtmxRequest htmxRequest) {
        if (htmxRequest.isHtmxRequest()) {
            return "fragments/cart/form";
        }

        model.addAttribute("promptCart", true);
        return "index";
    }

    @HxRequest
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToCart(@ModelAttribute("cart") Cart cart, @Valid @ModelAttribute CartItemDto itemDto, HtmxResponse htmxResponse) {
        var resolver = this.purchasableResolverRegistry.get(itemDto.itemType());
        var purchasable = resolver.resolve(itemDto.itemId());

        cart.addItem(purchasable, itemDto.quantity());
        htmxResponse.addTrigger("cart-size-changed", cart.getTotalQuantity());
        htmxResponse.addTrigger(Toast.PUSH_TOAST_EVENT, Toast.success("Added " + itemDto.quantity() + "x " + purchasable.getPurchasableName() + " to cart."));
    }

    @HxRequest
    @DeleteMapping
    public String removeFromCart(@ModelAttribute CartItemDto itemDto, @ModelAttribute("cart") Cart cart, HtmxResponse htmxResponse) {
        var resolver = this.purchasableResolverRegistry.get(itemDto.itemType());
        var purchasable = resolver.resolve(itemDto.itemId());

        cart.removeItem(purchasable);
        htmxResponse.addTrigger("cart-size-changed", cart.getTotalQuantity());
        htmxResponse.addTrigger(Toast.PUSH_TOAST_EVENT, Toast.success("Removed " + purchasable.getPurchasableName() + " from cart."));
        return "fragments/cart/form";
    }

    @HxRequest
    @PatchMapping
    public String setQuantity(@ModelAttribute CartItemDto itemDto, @ModelAttribute("cart") Cart cart, HtmxResponse htmxResponse) {
        var resolver = this.purchasableResolverRegistry.get(itemDto.itemType());
        var purchasable = resolver.resolve(itemDto.itemId());

        cart.setItemQuantity(purchasable, Math.clamp(itemDto.quantity().longValue(), CartItemDto.MIN_QUANTITY, CartItemDto.MAX_QUANTITY));
        htmxResponse.addTrigger("cart-size-changed", cart.getTotalQuantity());
        return "fragments/cart/form";
    }

    @HxRequest
    @PatchMapping("increment")
    public String incrementQuantity(@ModelAttribute CartItemDto itemDto, @ModelAttribute("cart") Cart cart, HtmxResponse htmxResponse) {
        var resolver = this.purchasableResolverRegistry.get(itemDto.itemType());
        var purchasable = resolver.resolve(itemDto.itemId());

        var quantity = Math.clamp(itemDto.quantity().longValue() + 1L, CartItemDto.MIN_QUANTITY, CartItemDto.MAX_QUANTITY);
        cart.setItemQuantity(purchasable, quantity);
        htmxResponse.addTrigger("cart-size-changed", cart.getTotalQuantity());
        return "fragments/cart/form";
    }

    @HxRequest
    @PatchMapping("decrement")
    public String decrementQuantity(@ModelAttribute CartItemDto itemDto, @ModelAttribute("cart") Cart cart, HtmxResponse htmxResponse) {
        var resolver = this.purchasableResolverRegistry.get(itemDto.itemType());
        var purchasable = resolver.resolve(itemDto.itemId());

        var quantity = Math.clamp(itemDto.quantity().longValue() - 1L, CartItemDto.MIN_QUANTITY, CartItemDto.MAX_QUANTITY);
        cart.setItemQuantity(purchasable, quantity);
        htmxResponse.addTrigger("cart-size-changed", cart.getTotalQuantity());
        return "fragments/cart/form";
    }

    @HxRequest
    @DeleteMapping("all")
    public String removeAllFromCart(@ModelAttribute("cart") Cart cart, HtmxResponse htmxResponse, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        htmxResponse.addTrigger("cart-size-changed", 0);
        htmxResponse.addTrigger(Toast.PUSH_TOAST_EVENT, Toast.success("Removed all items from cart."));
        return "fragments/cart/form";
    }
}
