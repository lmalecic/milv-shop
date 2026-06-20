package com.lmalecic.milvshop.controller.rest;

import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.Locale;

@RestController
@RequestMapping("/api/format")
public class FormatRestController {

    public static String CURRENCY = " €";

    @GetMapping("/price")
    public String price(@RequestParam Double value, @Nullable @RequestParam Double amount, @Nullable @RequestParam String locale) {
        Double totalPrice = amount == null ? value : amount * value;
        Locale effectiveLocale = Locale.forLanguageTag(locale != null ? locale : "hr-HR");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(effectiveLocale);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(totalPrice) + CURRENCY;
    }
}
