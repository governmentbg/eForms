package com.bulpros.eformsgateway.process.web.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TerminateProcessRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String businessKey;
    private String messageName;

}
