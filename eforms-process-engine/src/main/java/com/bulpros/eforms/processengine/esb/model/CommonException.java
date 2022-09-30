package com.bulpros.eforms.processengine.esb.model;

import com.bulpros.eforms.processengine.esb.model.enums.OriginSystem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonException {

    private OriginSystem failureOrigin;
    private List<Cause> cause;

}

