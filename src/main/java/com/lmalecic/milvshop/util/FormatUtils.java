package com.lmalecic.milvshop.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component("formats")
public class FormatUtils {

    private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("hr-HR");

    private FormatUtils() {}

    public static String price(BigDecimal value, Locale locale) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale != null ? locale : DEFAULT_LOCALE);
        return numberFormat.format(value);
    }
}
