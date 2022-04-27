package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ServiceSupplierDto implements Serializable {
    private static final long serialVersionUID = 1L;

    String code;
    String eik;
    String title;
}
