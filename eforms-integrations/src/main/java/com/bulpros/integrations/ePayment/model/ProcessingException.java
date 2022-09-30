package com.bulpros.integrations.ePayment.model;

import com.bulpros.integrations.esb.model.Cause;
import com.bulpros.integrations.esb.model.enums.OriginSystem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProcessingException {

    private OriginSystem failureOrigin;
    private List<Cause> cause;
    private Integer retryAfter;

}

