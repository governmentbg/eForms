package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminServiceFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> serviceStatuses;
    private String applicant;
    private String serviceSupplierId;
    private String serviceName;
}
