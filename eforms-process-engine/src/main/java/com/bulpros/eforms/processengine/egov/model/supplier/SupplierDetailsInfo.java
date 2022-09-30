package com.bulpros.eforms.processengine.egov.model.supplier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierDetailsInfo extends Supplier {
    boolean autSystem;
    String providerOID;
    String url;
    String logo;
    String contacts;
    String serviceProviderType;
    String status;
}
