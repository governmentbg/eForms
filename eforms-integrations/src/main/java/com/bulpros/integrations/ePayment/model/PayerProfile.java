package com.bulpros.integrations.ePayment.model;

import com.bulpros.integrations.esb.model.CommonTypeActor;
import com.bulpros.integrations.esb.model.enums.ParticipantTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayerProfile extends CommonTypeActor {
 
  private ParticipantTypeEnum participantType;

}

