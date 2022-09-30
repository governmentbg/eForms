package com.bulpros.eforms.processengine.camunda.model.enums;

public enum UserStageTypeEnum {
    REQUESTOR("requestor"),
    ADMINISTRATION("administration");

    public String stageType;

    UserStageTypeEnum(String stageType) {
        this.stageType = stageType;
    }
}
