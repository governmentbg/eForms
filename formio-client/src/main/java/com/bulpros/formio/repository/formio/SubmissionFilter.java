package com.bulpros.formio.repository.formio;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SubmissionFilter {
    private final SubmissionFilterClauseEnum clause;
    private final Map<String, Object> properties;
    
    private boolean isSubmissionData = true;
    
    public static SubmissionFilter build(String parameter, String value) {
        if (parameter.startsWith("data.")) {
            parameter = parameter.substring(5);
        }
        Matcher matcher = Pattern.compile("(.+)(__.*)$").matcher(parameter);
        if (matcher.find()) {
            String parameterName = matcher.group(1);
            
            String clauseString = matcher.group(2); 
            SubmissionFilterClauseEnum clause = SubmissionFilterClauseEnum.getByValue(clauseString);
            if (clause == null) {
                throw new IllegalArgumentException(String.format("Unsupported clause %s in parameter %s.", clauseString, parameter));
            }
            return new SubmissionFilter(clause, Collections.singletonMap(parameterName, value));
        }
        return new SubmissionFilter(SubmissionFilterClauseEnum.NONE, Collections.singletonMap(parameter, value));
    }
}
