package com.bulpros.eformsgateway.process.web.dto;

import com.bulpros.eformsgateway.process.service.BusinessKeyTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInstanceRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Object> variables;
    private BusinessKeyTypeEnum businessKeyType;
    private String applicant;
}
