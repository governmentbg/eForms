package com.bulpros.integrations.regix.model.NELK;

import com.bulpros.integrations.regix.model.Operation;

public enum NELKOperation implements Operation {
    GET_ALL_EXPERT_DECISION_BY_IDENTIFIER("TechnoLogica.RegiX.NelkEismeAdapter.APIService.INelkEismeAPI.GetAllExpertDecisionsByIdentifier");

    private final String key;

    private NELKOperation(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
