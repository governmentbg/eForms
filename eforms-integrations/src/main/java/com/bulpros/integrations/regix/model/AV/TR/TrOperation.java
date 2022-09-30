package com.bulpros.integrations.regix.model.AV.TR;

import com.bulpros.integrations.regix.model.Operation;

public enum TrOperation implements Operation {
    GET_ACTUAL_STATE("TechnoLogica.RegiX.AVTRAdapter.APIService.ITRAPI.GetActualState");

    private final String key;

    private TrOperation(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
