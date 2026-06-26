package com.lmalecic.milvshop.cart;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public final class CartItem {

    private Long itemId;
    private PurchasableType itemType;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private PurchasableAttributes attributes;

    @Setter
    private int quantity;

    public static CartItem of(Purchasable purchasable, int quantity) {
        return CartItem.builder()
                .itemId(purchasable.getPurchasableId())
                .itemType(purchasable.getPurchasableType())
                .name(purchasable.getPurchasableName())
                .imageUrl(purchasable.getPurchasableImageUrl())
                .price(purchasable.getPurchasablePrice())
                .quantity(quantity)
                .attributes(purchasable.getPurchasableAttributes())
                .build();
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public BigDecimal getSubtotal() {
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}
