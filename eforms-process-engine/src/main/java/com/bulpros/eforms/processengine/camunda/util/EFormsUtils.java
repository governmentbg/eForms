package com.bulpros.eforms.processengine.camunda.util;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
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
        String formApiPath = getFormApiPath(formKey);
        return Arrays.stream(formApiPath.trim().split("[ /]+"))
                .map(p -> {
                    if (p.contains("-")) {
                        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, p);
                    }
                    return p;
                })
                .collect(Collectors.joining("_"));
    }

    public static String getFormDataSubmissionKeyFromVariableName(String variableName) {
        return variableName.replace(ProcessConstants.SUBMISSION_DATA,"");
    }

    public static String getFormApiPathFromSubmissionKey(String submissionKey) {
        return Arrays.stream(submissionKey.trim().replace(ProcessConstants.SUBMISSION_DATA, "")
                .split("[ _]+"))
                .map(p -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, p))
                .collect(Collectors.joining("/"));
    }

    public static boolean isLocalVariableFormSubmission(String submissionKey) {
        return submissionKey.startsWith(ProcessConstants.SUBMISSION_DATA);
    }

}
