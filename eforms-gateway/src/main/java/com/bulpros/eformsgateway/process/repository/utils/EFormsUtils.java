package com.bulpros.eformsgateway.process.repository.utils;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class EFormsUtils {

    public static String getFormApiPath(String formKey) {
        if (formKey.contains("?")) {
            return StringUtils.substringBetween(formKey, ":/", "?");
        } else {
            return formKey.split(":/")[1];
        }
    }

    public static String getFormDataSubmissionKey(String formKey) {
        return Arrays.stream(formKey.trim().split("[ /]+"))
                .map(p -> {
                    if (p.contains("-")) {
                        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, p);
                    }
                    return p;
                })
                .collect(Collectors.joining("_"));
    }
}
