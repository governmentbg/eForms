package com.bulpros.eformsgateway.form.web.controller.dto;

public enum ProfileTypeEnum {
    PERSON          (1),
    LEGAL_PERSON    (2),
    INSTITUTION     (3);
    private int type;

    ProfileTypeEnum(int type) {
        this.type = type;
    }

    public static ProfileTypeEnum getByType(int type) {
        for(ProfileTypeEnum typeEnum : ProfileTypeEnum.values()){
            if( typeEnum.type == type) return typeEnum;
        }
        return null;
    }
}
