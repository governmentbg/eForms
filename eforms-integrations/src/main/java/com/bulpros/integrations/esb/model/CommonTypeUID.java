package com.bulpros.integrations.esb.model;

import com.bulpros.integrations.esb.model.enums.CommonTypeUIDEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonTypeUID {

    private CommonTypeUIDEnum type;
    private String value;

}

