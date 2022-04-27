package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullAdditionalProfileDto extends AdditionalProfileDto implements Serializable{
    private static final long serialVersionUID = 1L;

    private String personIdentifier;
    private String name;
}
