package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.egov.model.supplier.SupplierDetailsInfo;
import com.bulpros.eforms.processengine.egov.model.supplier.Suppliers;

public interface SupplierService {
    Suppliers getAllSuppliers();

    SupplierDetailsInfo getSupplierDetails(String code);
}
