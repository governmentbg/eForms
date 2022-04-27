
export interface AdministrativeUnit {
   administrationUnitEDelivery: String;
   administrationUnitID: String;
   administrationUnit: String;
}

export interface TaxValidityPeriod {
   taxValidTo: String;
   taxValidFrom: String;
   taxAmount: String;
}
export interface TaxCurrency {
   currency: String;
   taxValidityPeriodList: TaxValidityPeriod[];
}

export interface ServiceExecutionDeadline {
   deadlineType: String;
   hasPayment: String;
   deadlineTerm: String;
   taxCurrencyList: TaxCurrency[];
}

export interface ServiceSupplier {
   hasAdministrativeUnits: String;
   easAdministrativeUnitsList: AdministrativeUnit[];
   aisClientEPayment: String;
   serviceProviderBank: String;
   serviceProviderBIC: String;
   serviceProviderIBAN: String;
   paymentTypeCode: String;
   aisClientIntegrationKey: String;
   executionDeadlineList: ServiceExecutionDeadline[];
}

export interface DAEFServiceProvider {
    _id:String;
    owner:String;
    deleted:String;
    state:String;
    data:{
       code:Number;
       title:String;
       serviceProviderType:String;
       status:String;
       url: String;
       identifier: String;
       projectId: String;
       providerOID: String;
       contacts: String;
       hasAdministrativeUnits: String;
       administrativeUnitsList: AdministrativeUnit[];
    };
    form:String;
    project:String;
    created:String;
    modified:String;
    __v:Number
 }

export interface DAEFService {
    _id:String;
    owner:String;
    deleted:String;
    _vid:0;
    _fvid:0;
    state:String;
    data:{
       arId:Number;
       displayTitle:String;
       serviceDescription:String;
       requiredSecurityLevel:String;
       processDefinitionId:String;
       provisionEAddress:String;
       url:String;
       status:String;
       meansOfIdentification: String[];
       serviceOID: String;
       serviceSupplierList:Array<Number>;
    };
    form:String;
    project:String;
    created:String;
    modified:String;
    __v:Number
 }

 export interface DAEFServiceResponse {
    service: DAEFService;
    suppliers: DAEFServiceProvider[];
    existIncompleteCases: any;
    eDeliveryStatus: any;
 }