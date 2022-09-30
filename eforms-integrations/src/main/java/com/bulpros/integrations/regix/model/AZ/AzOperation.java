package com.bulpros.integrations.regix.model.AZ;

import com.bulpros.integrations.regix.model.Operation;

public enum AzOperation implements Operation {
    GET_JOB_SEEKER_STATUS("TechnoLogica.RegiX.AZJobsAdapter.APIService.IAZJobsAPI.GetJobSeekerStatus");

    private final String key;

    private AzOperation(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
