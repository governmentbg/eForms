package com.bulpros.integrations.regix.model.MON;

import com.bulpros.integrations.regix.model.Operation;

public enum MONOperation implements Operation {
    GET_CHILD_STUDENT_STATUS("TechnoLogica.RegiX.MonChildSchoolStudentsAdapter.APIService.IMonChildSchoolStudentsAPI.GetChildStudentStatus"),
    GET_HIGHER_EDU_STUDENT_BY_STATUS("TechnoLogica.RegiX.MonStudentsAdapter.APIService.IMonStudentsAPI.GetHigherEduStudentByStatus");

    private final String key;

    private MONOperation(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
