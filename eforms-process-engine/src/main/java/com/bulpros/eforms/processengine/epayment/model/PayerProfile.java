package com.bulpros.eforms.processengine.epayment.model;

import com.bulpros.eforms.processengine.esb.model.CommonTypeActor;
import com.bulpros.eforms.processengine.esb.model.CommonTypeInfo;
import com.bulpros.eforms.processengine.esb.model.CommonTypeUID;
import com.bulpros.eforms.processengine.esb.model.enums.CommonTypeActorEnum;
import com.bulpros.eforms.processengine.esb.model.enums.ParticipantTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayerProfile extends CommonTypeActor {

    private ParticipantTypeEnum participantType;

    @Builder
    public PayerProfile(ParticipantTypeEnum participantType, CommonTypeActorEnum type, CommonTypeUID uid,
                        String name, CommonTypeInfo info) {
        super(type, uid, name, info);
        this.participantType = participantType;
    }
}

