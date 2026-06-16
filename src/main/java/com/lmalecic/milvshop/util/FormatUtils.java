package com.lmalecic.milvshop.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component("formatUtils")
public class FormatUtils {

    public String getPriceString(BigDecimal price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("hr-HR"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(price) + " €";
    }
}
