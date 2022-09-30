package com.bulpros.eforms.processengine.epayment.model;

import com.bulpros.eforms.processengine.esb.model.Cause;
import com.bulpros.eforms.processengine.esb.model.enums.OriginSystem;
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

