package com.bulpros.integrations.esb.model;

import com.bulpros.integrations.esb.model.enums.CommonTypeActorEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonTypeActor {

    private CommonTypeActorEnum type;
    private CommonTypeUID uid;
    private String name;
    private CommonTypeInfo info;

}

