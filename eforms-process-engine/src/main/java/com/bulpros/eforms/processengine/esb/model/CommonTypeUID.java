package com.bulpros.eforms.processengine.esb.model;

import com.bulpros.eforms.processengine.esb.model.enums.CommonTypeUIDEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonTypeUID {

    private CommonTypeUIDEnum type;
    private String value;

}

