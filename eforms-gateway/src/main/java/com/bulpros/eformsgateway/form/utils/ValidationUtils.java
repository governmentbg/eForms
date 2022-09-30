package com.bulpros.eformsgateway.form.utils;

import com.bulpros.eformsgateway.form.service.CaseStatusClassifierEnum;
import com.bulpros.eformsgateway.form.service.ServiceWithSuppliersStatusEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ValidationUtils {
    public static List<String> getValidCaseClassifiers(String classifier) {
        var classifiers = classifier.split("\\s*,\\s*");
        return Arrays.stream(classifiers).map(status -> {
            try {
                return CaseStatusClassifierEnum.getCaseStatusEnumByStatus(status).classifier;
            } catch (Exception e) {
                return null;
            }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }

    public static boolean isServiceStatusValid(String status) {
        return Arrays.stream(ServiceWithSuppliersStatusEnum.values())
                .anyMatch(e -> e.status.equals(status));
    }

    public static boolean isNotPresent(String property) {
        return property == null || property.isEmpty();
    }
}
