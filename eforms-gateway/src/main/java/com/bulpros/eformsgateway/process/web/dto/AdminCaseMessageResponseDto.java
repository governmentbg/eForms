package com.bulpros.eformsgateway.process.web.dto;

import com.bulpros.eformsgateway.process.web.dto.enums.AdminCaseMessageResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCaseMessageResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private HttpStatus status;


}
