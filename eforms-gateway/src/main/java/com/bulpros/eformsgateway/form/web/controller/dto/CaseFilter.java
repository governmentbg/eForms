package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class CaseFilter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Pattern(regexp ="[^~!@#$%^&*+=|<>]*")
    private String businessKey;
    @Pattern(regexp ="[^~!@#$%^&*+=|<>]*")
    private String serviceName;
    @Pattern(regexp ="[^~!@#$%^&*+=|<>]*")
    private String serviceUri;
    private String requestor;
    private String applicant;
}
