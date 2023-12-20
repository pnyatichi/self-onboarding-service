package com.coopbank.selfonboarding.util;

import org.apache.commons.text.StringEscapeUtils;

public class SanitizerUtils {
    public static String sanitizeInput(String input) {
        // Use Apache Commons Text to sanitize input
        return StringEscapeUtils.escapeHtml4(input);
    }
}
