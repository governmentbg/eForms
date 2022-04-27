package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;

import com.bulpros.formio.dto.ResourceDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSubmissionResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private ResourceDto service;
    private Boolean existIncompleteCases;
    @JsonProperty("eDeliveryStatus")//Json serializer lowers the letter D, thus we need to enforce it to keep the case
    private String eDeliveryStatus;
}
