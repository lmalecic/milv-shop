package com.lmalecic.milvshop.cart;

import com.lmalecic.milvshop.util.FormatUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class Cart {

    private static final String KEY_SEPARATOR = ":";

    @Getter
    private final Map<String, CartItem> items = new HashMap<>();

    public void addItem(Purchasable purchasable, int quantity) {
        String key = this.getKey(purchasable);
        if (this.items.containsKey(key)) {
            this.items.get(key).increaseQuantity(quantity);
        } else {
            this.items.put(key, CartItem.of(purchasable, quantity));
        }
    }

    public void removeItem(Purchasable purchasable) {
        this.items.remove(this.getKey(purchasable));
    }

    public void setItemQuantity(Purchasable purchasable, int quantity) {
        var key = this.getKey(purchasable);
        if (!this.items.containsKey(key)) {
            return;
        }

        this.items.get(key).setQuantity(quantity);
    }

    public boolean contains(String type, String id) {
        return this.items.containsKey(type + KEY_SEPARATOR + id);
    }

    public BigDecimal getTotal() {
        return this.items.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getTotalFormatted() {
        return FormatUtils.price(this.getTotal(), LocaleContextHolder.getLocale());
    }

    public Integer getTotalQuantity() {
        return this.items.values().stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum);
    }

    private String getKey(Purchasable purchasable) {
        return purchasable.getPurchasableType().name() + KEY_SEPARATOR + purchasable.getPurchasableId().toString();
    }
}
