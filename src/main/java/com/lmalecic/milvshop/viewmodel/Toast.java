package com.lmalecic.milvshop.viewmodel;

import com.fasterxml.jackson.annotation.JsonValue;

public record Toast(String message, ToastType type) {

    public static final String PUSH_TOAST_EVENT = "pushToast";

    public enum ToastType {
        SUCCESS,
        ERROR,
        INFO;

        @JsonValue
        public String toJson() {
            return this.name().toLowerCase();
        }
    }

    public static Toast success(String message) {
        return new Toast(message, ToastType.SUCCESS);
    }

    public static Toast error(String message) {
        return new Toast(message, ToastType.ERROR);
    }

    public static Toast info(String message) {
        return new Toast(message, ToastType.INFO);
    }
}
