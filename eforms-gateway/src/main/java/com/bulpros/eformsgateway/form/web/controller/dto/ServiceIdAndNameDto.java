package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceIdAndNameDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceId;
    private String serviceName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceIdAndNameDto that = (ServiceIdAndNameDto) o;
        return Objects.equals(serviceId, that.serviceId) && Objects.equals(serviceName, that.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, serviceName);
    }
}
