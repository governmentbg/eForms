package com.bulpros.eforms.processengine.esb.model;

import com.bulpros.eforms.processengine.esb.model.enums.CommonTypeActorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonTypeActor {

    private CommonTypeActorEnum type;
    private CommonTypeUID uid;
    private String name;
    private CommonTypeInfo info;

}

