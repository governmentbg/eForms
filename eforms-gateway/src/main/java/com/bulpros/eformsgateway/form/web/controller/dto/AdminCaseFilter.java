package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AdminCaseFilter extends CaseFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceId;
    @Pattern(regexp ="[^~!@#$%^&*+=|<>]*")
    private String requestorName;
    private List<String> processInstanceIds;
    private List<String> userIds;
    private String serviceSupplierId;
    private String fromIssueDate;
    private String toIssueDate;
    private Boolean assignedToMe;
    
    public AdminCaseFilter(String businessKey, String requestor, String applicant, String serviceName,
                           String serviceId) {
        super(businessKey, requestor, applicant, serviceName, serviceId);
    }
}
