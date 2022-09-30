package com.bulpros.integrations.borica.model;

import com.bulpros.integrations.eDelivery.model.EProfileType;

public enum BoricaIdentificatorTypeEnum {
    EGN, LNC, PHONE, EMAIL;

    public static BoricaIdentificatorTypeEnum fromKey(String key) {
        for (BoricaIdentificatorTypeEnum item: BoricaIdentificatorTypeEnum.values()) {
            if (item.name().equals(key)) {
                return item;
            }
        }
        return null;
    }
}
