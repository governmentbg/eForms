package com.bulpros.integrations.exceptions;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionBody {
    private final int status;
    private final String error;
    private final String message;
    private final Object data;

}
