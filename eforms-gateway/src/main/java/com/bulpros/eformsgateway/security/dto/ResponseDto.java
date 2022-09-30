package com.bulpros.eformsgateway.security.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto implements Serializable {
    private static final long serialVersionUID = 7438255714694047836L;

    private HttpStatus status;
    private byte[] body;
}
