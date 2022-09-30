package com.bulpros.integrations.regix.model.GRAO.PersonDataSearch;

import com.bulpros.integrations.regix.model.Operation;

public enum GraoOperation implements Operation {
    PERSON_DATA_SEARCH("TechnoLogica.RegiX.GraoNBDAdapter.APIService.INBDAPI.PersonDataSearch"),
    MARITAL_STATUS_SEARCH("TechnoLogica.RegiX.GraoBRAdapter.APIService.IBRAPI.MaritalStatusSearch"),
    MARITAL_STATUS_SPOUSE_CHILDREN_SEARCH("TechnoLogica.RegiX.GraoBRAdapter.APIService.IBRAPI.MaritalStatusSpouseChildrenSearch"),
    PERMANENT_ADDRESS_SEARCH("TechnoLogica.RegiX.GraoPNAAdapter.APIService.IPNAAPI.PermanentAddressSearch"),
    RELATIONS_SEARCH("TechnoLogica.RegiX.GraoNBDAdapter.APIService.INBDAPI.RelationsSearch"),
    TEMPORARY_ADDRESS_SEARCH("TechnoLogica.RegiX.GraoPNAAdapter.APIService.IPNAAPI.TemporaryAddressSearch");

    private final String key;

    private GraoOperation(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
