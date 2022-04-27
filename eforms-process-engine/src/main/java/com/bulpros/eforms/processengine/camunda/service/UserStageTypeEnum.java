package com.bulpros.eforms.processengine.camunda.service;

public enum UserStageTypeEnum {
    REQUESTOR       ("requestor"),
    ADMINISTRATION   ("administration");

    public String stageType;

    UserStageTypeEnum(String stageType) {
        this.stageType = stageType;
    }
}
