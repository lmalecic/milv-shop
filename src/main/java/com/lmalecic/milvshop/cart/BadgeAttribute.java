package com.lmalecic.milvshop.cart;

public record BadgeAttribute(String text, String imgUrl) {

    public BadgeAttribute(String text) {
        this(text, null);
    }

}
