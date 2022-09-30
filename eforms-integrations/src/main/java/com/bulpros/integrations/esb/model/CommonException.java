package com.bulpros.integrations.esb.model;

import com.bulpros.integrations.esb.model.enums.OriginSystem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonException {

    private OriginSystem failureOrigin;
    private List<Cause> cause;

}

