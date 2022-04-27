package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.egov.model.eservice.EServiceDetails;
import com.bulpros.eforms.processengine.egov.model.eservice.EServiceList;

import java.util.List;

public interface EServiceService {
    EServiceList getServicesBySupplierEIK(String supplierEIK);
    EServiceDetails getServiceDetailsByNumber(List<String> serviceNumberList);
}
