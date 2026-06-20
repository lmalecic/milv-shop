package com.lmalecic.milvshop.component;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component("formatUtils")
public class FormatUtils {

    public <T extends Number> String getPriceString(T price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("hr-HR"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(price) + " €";
    }
}
