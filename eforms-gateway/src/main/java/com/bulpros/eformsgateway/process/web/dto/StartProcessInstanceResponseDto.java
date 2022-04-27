package com.bulpros.eformsgateway.process.web.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StartProcessInstanceResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String businessKey;
}
