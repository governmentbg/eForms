package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public
class ResourceDataDto<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public T data;
}
