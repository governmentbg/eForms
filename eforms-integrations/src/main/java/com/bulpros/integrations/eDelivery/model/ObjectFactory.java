
package com.bulpros.integrations.eDelivery.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bulpros.integrations.eDelivery.model package. 
 * &lt;p&gt;An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ArrayOfDcInstitutionInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "ArrayOfDcInstitutionInfo");
    private final static QName _DcInstitutionInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcInstitutionInfo");
    private final static QName _DcSubjectInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcSubjectInfo");
    private final static QName _DcSubjectPublicInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcSubjectPublicInfo");
    private final static QName _DcAdministrativeActInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcAdministrativeActInfo");
    private final static QName _DcCertificateInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcCertificateInfo");
    private final static QName _WebInstitutionInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "WebInstitutionInfo");
    private final static QName _DcLegalPersonInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcLegalPersonInfo");
    private final static QName _DcPersonInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcPersonInfo");
    private final static QName _WebLegalPersonInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "WebLegalPersonInfo");
    private final static QName _DcTokenVerificationInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcTokenVerificationInfo");
    private final static QName _DcElectronicIdentityInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcElectronicIdentityInfo");
    private final static QName _DcAddress_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcAddress");
    private final static QName _ArrayOfDcSubjectPublicInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "ArrayOfDcSubjectPublicInfo");
    private final static QName _DcMessageWithCodeReceiver_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DcMessageWithCodeReceiver");
    private final static QName _ArrayOfDcDocumentAdditional_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ArrayOfDcDocumentAdditional");
    private final static QName _DcDocumentAdditional_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcDocumentAdditional");
    private final static QName _DcDocument_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcDocument");
    private final static QName _ArrayOfDcSignatureValidationResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ArrayOfDcSignatureValidationResult");
    private final static QName _DcSignatureValidationResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcSignatureValidationResult");
    private final static QName _ArrayOfDcChainCertificate_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ArrayOfDcChainCertificate");
    private final static QName _DcChainCertificate_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcChainCertificate");
    private final static QName _DcTimeStamp_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcTimeStamp");
    private final static QName _ArrayOfDcDocument_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ArrayOfDcDocument");
    private final static QName _DcMessageDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcMessageDetails");
    private final static QName _DcMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcMessage");
    private final static QName _DcLogin_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcLogin");
    private final static QName _ArrayOfDcProfile_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ArrayOfDcProfile");
    private final static QName _DcProfile_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcProfile");
    private final static QName _DcTimeStampMessageContent_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcTimeStampMessageContent");
    private final static QName _ArrayOfDcMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ArrayOfDcMessage");
    private final static QName _DcPartialListOfDcMessageHR29GRRX_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcPartialListOfDcMessageHR29gRRX");
    private final static QName _DcPersonRegistrationInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcPersonRegistrationInfo");
    private final static QName _ArrayOfDcSubjectShortInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ArrayOfDcSubjectShortInfo");
    private final static QName _DcSubjectShortInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcSubjectShortInfo");
    private final static QName _DcLegalPersonRegistrationInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcLegalPersonRegistrationInfo");
    private final static QName _DcSubjectRegistrationInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcSubjectRegistrationInfo");
    private final static QName _DcRegisteredSubjectInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcRegisteredSubjectInfo");
    private final static QName _DcStatisticsGeneral_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DcStatisticsGeneral");
    private final static QName _ArrayOfstring_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfstring");
    private final static QName _ArrayOfanyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfanyType");
    private final static QName _EProfileType_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", "eProfileType");
    private final static QName _EVerificationInfoType_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", "eVerificationInfoType");
    private final static QName _ERevokationResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", "eRevokationResult");
    private final static QName _EVerificationResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", "eVerificationResult");
    private final static QName _ESortColumn_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", "eSortColumn");
    private final static QName _ESortOrder_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", "eSortOrder");
    private final static QName _EInstitutionType_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", "eInstitutionType");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _GetRegisteredInstitutionsResponseGetRegisteredInstitutionsResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetRegisteredInstitutionsResult");
    private final static QName _SendElectronicDocumentSubject_QNAME = new QName("https://edelivery.egov.bg/services/integration", "subject");
    private final static QName _SendElectronicDocumentDocBytes_QNAME = new QName("https://edelivery.egov.bg/services/integration", "docBytes");
    private final static QName _SendElectronicDocumentDocNameWithExtension_QNAME = new QName("https://edelivery.egov.bg/services/integration", "docNameWithExtension");
    private final static QName _SendElectronicDocumentDocRegNumber_QNAME = new QName("https://edelivery.egov.bg/services/integration", "docRegNumber");
    private final static QName _SendElectronicDocumentReceiverUniqueIdentifier_QNAME = new QName("https://edelivery.egov.bg/services/integration", "receiverUniqueIdentifier");
    private final static QName _SendElectronicDocumentReceiverPhone_QNAME = new QName("https://edelivery.egov.bg/services/integration", "receiverPhone");
    private final static QName _SendElectronicDocumentReceiverEmail_QNAME = new QName("https://edelivery.egov.bg/services/integration", "receiverEmail");
    private final static QName _SendElectronicDocumentServiceOID_QNAME = new QName("https://edelivery.egov.bg/services/integration", "serviceOID");
    private final static QName _SendElectronicDocumentOperatorEGN_QNAME = new QName("https://edelivery.egov.bg/services/integration", "operatorEGN");
    private final static QName _SendElectronicDocumentWithAccessCodeReceiver_QNAME = new QName("https://edelivery.egov.bg/services/integration", "receiver");
    private final static QName _SendElectronicDocumentOnBehalfOfSenderUniqueIdentifier_QNAME = new QName("https://edelivery.egov.bg/services/integration", "senderUniqueIdentifier");
    private final static QName _SendElectronicDocumentOnBehalfOfSenderPhone_QNAME = new QName("https://edelivery.egov.bg/services/integration", "senderPhone");
    private final static QName _SendElectronicDocumentOnBehalfOfSenderEmail_QNAME = new QName("https://edelivery.egov.bg/services/integration", "senderEmail");
    private final static QName _SendElectronicDocumentOnBehalfOfSenderFirstName_QNAME = new QName("https://edelivery.egov.bg/services/integration", "senderFirstName");
    private final static QName _SendElectronicDocumentOnBehalfOfSenderLastName_QNAME = new QName("https://edelivery.egov.bg/services/integration", "senderLastName");
    private final static QName _SendMessageMessage_QNAME = new QName("https://edelivery.egov.bg/services/integration", "message");
    private final static QName _GetSentDocumentStatusByRegNumDocumentRegistrationNumber_QNAME = new QName("https://edelivery.egov.bg/services/integration", "documentRegistrationNumber");
    private final static QName _GetSentDocumentStatusByRegNumResponseGetSentDocumentStatusByRegNumResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSentDocumentStatusByRegNumResult");
    private final static QName _GetSentDocumentContentByRegNumResponseGetSentDocumentContentByRegNumResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSentDocumentContentByRegNumResult");
    private final static QName _GetSentDocumentsContentResponseGetSentDocumentsContentResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSentDocumentsContentResult");
    private final static QName _GetSentDocumentContentResponseGetSentDocumentContentResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSentDocumentContentResult");
    private final static QName _GetSentMessageStatusResponseGetSentMessageStatusResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSentMessageStatusResult");
    private final static QName _GetSentMessagesListResponseGetSentMessagesListResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSentMessagesListResult");
    private final static QName _GetSentMessagesListPagedResponseGetSentMessagesListPagedResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSentMessagesListPagedResult");
    private final static QName _GetReceivedMessagesListResponseGetReceivedMessagesListResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetReceivedMessagesListResult");
    private final static QName _GetReceivedMessagesListPagedResponseGetReceivedMessagesListPagedResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetReceivedMessagesListPagedResult");
    private final static QName _GetSentMessageContentResponseGetSentMessageContentResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSentMessageContentResult");
    private final static QName _GetReceivedMessageContentResponseGetReceivedMessageContentResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetReceivedMessageContentResult");
    private final static QName _CheckPersonHasRegistrationPersonId_QNAME = new QName("https://edelivery.egov.bg/services/integration", "personId");
    private final static QName _CheckPersonHasRegistrationResponseCheckPersonHasRegistrationResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "CheckPersonHasRegistrationResult");
    private final static QName _CheckLegalPersonHasRegistrationEik_QNAME = new QName("https://edelivery.egov.bg/services/integration", "eik");
    private final static QName _CheckLegalPersonHasRegistrationResponseCheckLegalPersonHasRegistrationResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "CheckLegalPersonHasRegistrationResult");
    private final static QName _CheckSubjectHasRegistrationIdentificator_QNAME = new QName("https://edelivery.egov.bg/services/integration", "identificator");
    private final static QName _CheckSubjectHasRegistrationResponseCheckSubjectHasRegistrationResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "CheckSubjectHasRegistrationResult");
    private final static QName _GetSubjectInfoResponseGetSubjectInfoResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetSubjectInfoResult");
    private final static QName _GetEDeliveryGeneralStatisticsResponseGetEDeliveryGeneralStatisticsResult_QNAME = new QName("https://edelivery.egov.bg/services/integration", "GetEDeliveryGeneralStatisticsResult");
    private final static QName _SendMessageOnBehalfToPersonReceiverFirstName_QNAME = new QName("https://edelivery.egov.bg/services/integration", "receiverFirstName");
    private final static QName _SendMessageOnBehalfToPersonReceiverLastName_QNAME = new QName("https://edelivery.egov.bg/services/integration", "receiverLastName");
    private final static QName _DcSubjectPublicInfoElectronicSubjectName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "ElectronicSubjectName");
    private final static QName _DcSubjectPublicInfoEmail_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "Email");
    private final static QName _DcSubjectPublicInfoPhoneNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "PhoneNumber");
    private final static QName _DcRegisteredSubjectInfoInstitutionType_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "InstitutionType");
    private final static QName _DcSubjectRegistrationInfoIdentificator_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Identificator");
    private final static QName _DcSubjectRegistrationInfoSubjectInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "SubjectInfo");
    private final static QName _DcLegalPersonRegistrationInfoEIK_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "EIK");
    private final static QName _DcLegalPersonRegistrationInfoEmail_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Email");
    private final static QName _DcLegalPersonRegistrationInfoName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Name");
    private final static QName _DcLegalPersonRegistrationInfoPhone_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Phone");
    private final static QName _DcLegalPersonRegistrationInfoProfilesWithAccess_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ProfilesWithAccess");
    private final static QName _DcSubjectShortInfoEGN_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "EGN");
    private final static QName _DcPersonRegistrationInfoAccessibleProfiles_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "AccessibleProfiles");
    private final static QName _DcPersonRegistrationInfoPersonIdentificator_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "PersonIdentificator");
    private final static QName _DcPartialListOfDcMessageHR29GRRXItems_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Items");
    private final static QName _DcTimeStampMessageContentContent_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Content");
    private final static QName _DcTimeStampMessageContentContentType_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ContentType");
    private final static QName _DcTimeStampMessageContentFileName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "FileName");
    private final static QName _DcProfileElectronicSubjectName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ElectronicSubjectName");
    private final static QName _DcLoginCertificateThumbprint_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "CertificateThumbprint");
    private final static QName _DcLoginPhoneNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "PhoneNumber");
    private final static QName _DcLoginProfiles_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Profiles");
    private final static QName _DcLoginPushNotificationsUrl_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "PushNotificationsUrl");
    private final static QName _DcMessageDateReceived_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DateReceived");
    private final static QName _DcMessageDateSent_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DateSent");
    private final static QName _DcMessageReceiverLogin_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ReceiverLogin");
    private final static QName _DcMessageReceiverProfile_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ReceiverProfile");
    private final static QName _DcMessageSenderLogin_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "SenderLogin");
    private final static QName _DcMessageSenderProfile_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "SenderProfile");
    private final static QName _DcMessageTitle_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Title");
    private final static QName _DcMessageDetailsAttachedDocuments_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "AttachedDocuments");
    private final static QName _DcMessageDetailsMessageText_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "MessageText");
    private final static QName _DcMessageDetailsTimeStampContent_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "TimeStampContent");
    private final static QName _DcMessageDetailsTimeStampNRD_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "TimeStampNRD");
    private final static QName _DcMessageDetailsTimeStampNRO_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "TimeStampNRO");
    private final static QName _DcTimeStampTimeStampData_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "TimeStampData");
    private final static QName _DcChainCertificateSubject_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "Subject");
    private final static QName _DcDocumentContentEncodingCodePage_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "ContentEncodingCodePage");
    private final static QName _DcDocumentDocumentName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DocumentName");
    private final static QName _DcDocumentDocumentRegistrationNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DocumentRegistrationNumber");
    private final static QName _DcDocumentSignaturesInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "SignaturesInfo");
    private final static QName _DcDocumentTimeStamp_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "TimeStamp");
    private final static QName _DcDocumentAdditionalCreatedBy_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "CreatedBy");
    private final static QName _DcDocumentAdditionalDocumentDescription_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", "DocumentDescription");
    private final static QName _DcMessageWithCodeReceiverEGNorLNCH_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "EGNorLNCH");
    private final static QName _DcMessageWithCodeReceiverFirstName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "FirstName");
    private final static QName _DcMessageWithCodeReceiverLastName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "LastName");
    private final static QName _DcMessageWithCodeReceiverMiddleName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "MiddleName");
    private final static QName _DcMessageWithCodeReceiverPhone_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "Phone");
    private final static QName _DcAddressAddress_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "Address");
    private final static QName _DcAddressCity_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "City");
    private final static QName _DcAddressCountryIso2_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "CountryIso2");
    private final static QName _DcAddressState_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "State");
    private final static QName _DcAddressZipCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "ZipCode");
    private final static QName _DcElectronicIdentityInfoDateOfBirth_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DateOfBirth");
    private final static QName _DcElectronicIdentityInfoEGN_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "EGN");
    private final static QName _DcElectronicIdentityInfoFamilyName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "FamilyName");
    private final static QName _DcElectronicIdentityInfoFamilyNameLat_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "FamilyNameLat");
    private final static QName _DcElectronicIdentityInfoGivenName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "GivenName");
    private final static QName _DcElectronicIdentityInfoGivenNameLat_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "GivenNameLat");
    private final static QName _DcElectronicIdentityInfoMiddleNameLat_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "MiddleNameLat");
    private final static QName _DcElectronicIdentityInfoPID_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "PID");
    private final static QName _DcElectronicIdentityInfoSpin_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "Spin");
    private final static QName _DcTokenVerificationInfoToken_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "Token");
    private final static QName _DcSubjectInfoDateCreated_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DateCreated");
    private final static QName _DcSubjectInfoUniqueSubjectIdentifier_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "UniqueSubjectIdentifier");
    private final static QName _DcSubjectInfoVerificationInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "VerificationInfo");
    private final static QName _DcLegalPersonInfoCompanyName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "CompanyName");
    private final static QName _DcLegalPersonInfoDateOutOfForce_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DateOutOfForce");
    private final static QName _DcLegalPersonInfoRegisteredBy_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "RegisteredBy");
    private final static QName _WebLegalPersonInfoDateDeleted_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DateDeleted");
    private final static QName _WebLegalPersonInfoRegistrationDcouments_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "RegistrationDcouments");
    private final static QName _DcPersonInfoDateOfDeath_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "DateOfDeath");
    private final static QName _DcInstitutionInfoHeadInstitution_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "HeadInstitution");
    private final static QName _DcInstitutionInfoName_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "Name");
    private final static QName _DcInstitutionInfoSubInstitutions_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "SubInstitutions");
    private final static QName _WebInstitutionInfoAdditionalDcouments_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "AdditionalDcouments");
    private final static QName _WebInstitutionInfoRegistrationDocument_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "RegistrationDocument");
    private final static QName _DcCertificateInfoIssuer_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "Issuer");
    private final static QName _DcCertificateInfoSubject_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "Subject");
    private final static QName _DcAdministrativeActInfoActNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "ActNumber");
    private final static QName _DcAdministrativeActInfoCreatedByInstitution_QNAME = new QName("http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", "CreatedByInstitution");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bulpros.integrations.eDelivery.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArrayOfDcInstitutionInfo }
     * 
     */
    public ArrayOfDcInstitutionInfo createArrayOfDcInstitutionInfo() {
        return new ArrayOfDcInstitutionInfo();
    }

    /**
     * Create an instance of {@link DcInstitutionInfo }
     * 
     */
    public DcInstitutionInfo createDcInstitutionInfo() {
        return new DcInstitutionInfo();
    }

    /**
     * Create an instance of {@link DcSubjectInfo }
     * 
     */
    public DcSubjectInfo createDcSubjectInfo() {
        return new DcSubjectInfo();
    }

    /**
     * Create an instance of {@link DcSubjectPublicInfo }
     * 
     */
    public DcSubjectPublicInfo createDcSubjectPublicInfo() {
        return new DcSubjectPublicInfo();
    }

    /**
     * Create an instance of {@link DcAdministrativeActInfo }
     * 
     */
    public DcAdministrativeActInfo createDcAdministrativeActInfo() {
        return new DcAdministrativeActInfo();
    }

    /**
     * Create an instance of {@link DcCertificateInfo }
     * 
     */
    public DcCertificateInfo createDcCertificateInfo() {
        return new DcCertificateInfo();
    }

    /**
     * Create an instance of {@link WebInstitutionInfo }
     * 
     */
    public WebInstitutionInfo createWebInstitutionInfo() {
        return new WebInstitutionInfo();
    }

    /**
     * Create an instance of {@link DcLegalPersonInfo }
     * 
     */
    public DcLegalPersonInfo createDcLegalPersonInfo() {
        return new DcLegalPersonInfo();
    }

    /**
     * Create an instance of {@link DcPersonInfo }
     * 
     */
    public DcPersonInfo createDcPersonInfo() {
        return new DcPersonInfo();
    }

    /**
     * Create an instance of {@link WebLegalPersonInfo }
     * 
     */
    public WebLegalPersonInfo createWebLegalPersonInfo() {
        return new WebLegalPersonInfo();
    }

    /**
     * Create an instance of {@link DcTokenVerificationInfo }
     * 
     */
    public DcTokenVerificationInfo createDcTokenVerificationInfo() {
        return new DcTokenVerificationInfo();
    }

    /**
     * Create an instance of {@link DcElectronicIdentityInfo }
     * 
     */
    public DcElectronicIdentityInfo createDcElectronicIdentityInfo() {
        return new DcElectronicIdentityInfo();
    }

    /**
     * Create an instance of {@link DcAddress }
     * 
     */
    public DcAddress createDcAddress() {
        return new DcAddress();
    }

    /**
     * Create an instance of {@link ArrayOfDcSubjectPublicInfo }
     * 
     */
    public ArrayOfDcSubjectPublicInfo createArrayOfDcSubjectPublicInfo() {
        return new ArrayOfDcSubjectPublicInfo();
    }

    /**
     * Create an instance of {@link DcMessageWithCodeReceiver }
     * 
     */
    public DcMessageWithCodeReceiver createDcMessageWithCodeReceiver() {
        return new DcMessageWithCodeReceiver();
    }

    /**
     * Create an instance of {@link ArrayOfDcDocumentAdditional }
     * 
     */
    public ArrayOfDcDocumentAdditional createArrayOfDcDocumentAdditional() {
        return new ArrayOfDcDocumentAdditional();
    }

    /**
     * Create an instance of {@link DcDocumentAdditional }
     * 
     */
    public DcDocumentAdditional createDcDocumentAdditional() {
        return new DcDocumentAdditional();
    }

    /**
     * Create an instance of {@link DcDocument }
     * 
     */
    public DcDocument createDcDocument() {
        return new DcDocument();
    }

    /**
     * Create an instance of {@link ArrayOfDcSignatureValidationResult }
     * 
     */
    public ArrayOfDcSignatureValidationResult createArrayOfDcSignatureValidationResult() {
        return new ArrayOfDcSignatureValidationResult();
    }

    /**
     * Create an instance of {@link DcSignatureValidationResult }
     * 
     */
    public DcSignatureValidationResult createDcSignatureValidationResult() {
        return new DcSignatureValidationResult();
    }

    /**
     * Create an instance of {@link ArrayOfDcChainCertificate }
     * 
     */
    public ArrayOfDcChainCertificate createArrayOfDcChainCertificate() {
        return new ArrayOfDcChainCertificate();
    }

    /**
     * Create an instance of {@link DcChainCertificate }
     * 
     */
    public DcChainCertificate createDcChainCertificate() {
        return new DcChainCertificate();
    }

    /**
     * Create an instance of {@link DcTimeStamp }
     * 
     */
    public DcTimeStamp createDcTimeStamp() {
        return new DcTimeStamp();
    }

    /**
     * Create an instance of {@link ArrayOfDcDocument }
     * 
     */
    public ArrayOfDcDocument createArrayOfDcDocument() {
        return new ArrayOfDcDocument();
    }

    /**
     * Create an instance of {@link DcMessageDetails }
     * 
     */
    public DcMessageDetails createDcMessageDetails() {
        return new DcMessageDetails();
    }

    /**
     * Create an instance of {@link DcMessage }
     * 
     */
    public DcMessage createDcMessage() {
        return new DcMessage();
    }

    /**
     * Create an instance of {@link DcLogin }
     * 
     */
    public DcLogin createDcLogin() {
        return new DcLogin();
    }

    /**
     * Create an instance of {@link ArrayOfDcProfile }
     * 
     */
    public ArrayOfDcProfile createArrayOfDcProfile() {
        return new ArrayOfDcProfile();
    }

    /**
     * Create an instance of {@link DcProfile }
     * 
     */
    public DcProfile createDcProfile() {
        return new DcProfile();
    }

    /**
     * Create an instance of {@link DcTimeStampMessageContent }
     * 
     */
    public DcTimeStampMessageContent createDcTimeStampMessageContent() {
        return new DcTimeStampMessageContent();
    }

    /**
     * Create an instance of {@link ArrayOfDcMessage }
     * 
     */
    public ArrayOfDcMessage createArrayOfDcMessage() {
        return new ArrayOfDcMessage();
    }

    /**
     * Create an instance of {@link DcPartialListOfDcMessageHR29GRRX }
     * 
     */
    public DcPartialListOfDcMessageHR29GRRX createDcPartialListOfDcMessageHR29GRRX() {
        return new DcPartialListOfDcMessageHR29GRRX();
    }

    /**
     * Create an instance of {@link DcPersonRegistrationInfo }
     * 
     */
    public DcPersonRegistrationInfo createDcPersonRegistrationInfo() {
        return new DcPersonRegistrationInfo();
    }

    /**
     * Create an instance of {@link ArrayOfDcSubjectShortInfo }
     * 
     */
    public ArrayOfDcSubjectShortInfo createArrayOfDcSubjectShortInfo() {
        return new ArrayOfDcSubjectShortInfo();
    }

    /**
     * Create an instance of {@link DcSubjectShortInfo }
     * 
     */
    public DcSubjectShortInfo createDcSubjectShortInfo() {
        return new DcSubjectShortInfo();
    }

    /**
     * Create an instance of {@link DcLegalPersonRegistrationInfo }
     * 
     */
    public DcLegalPersonRegistrationInfo createDcLegalPersonRegistrationInfo() {
        return new DcLegalPersonRegistrationInfo();
    }

    /**
     * Create an instance of {@link DcSubjectRegistrationInfo }
     * 
     */
    public DcSubjectRegistrationInfo createDcSubjectRegistrationInfo() {
        return new DcSubjectRegistrationInfo();
    }

    /**
     * Create an instance of {@link DcRegisteredSubjectInfo }
     * 
     */
    public DcRegisteredSubjectInfo createDcRegisteredSubjectInfo() {
        return new DcRegisteredSubjectInfo();
    }

    /**
     * Create an instance of {@link DcStatisticsGeneral }
     * 
     */
    public DcStatisticsGeneral createDcStatisticsGeneral() {
        return new DcStatisticsGeneral();
    }

    /**
     * Create an instance of {@link ArrayOfstring }
     * 
     */
    public ArrayOfstring createArrayOfstring() {
        return new ArrayOfstring();
    }

    /**
     * Create an instance of {@link ArrayOfanyType }
     * 
     */
    public ArrayOfanyType createArrayOfanyType() {
        return new ArrayOfanyType();
    }

    /**
     * Create an instance of {@link GetRegisteredInstitutions }
     * 
     */
    public GetRegisteredInstitutions createGetRegisteredInstitutions() {
        return new GetRegisteredInstitutions();
    }

    /**
     * Create an instance of {@link GetRegisteredInstitutionsResponse }
     * 
     */
    public GetRegisteredInstitutionsResponse createGetRegisteredInstitutionsResponse() {
        return new GetRegisteredInstitutionsResponse();
    }

    /**
     * Create an instance of {@link SendElectronicDocument }
     * 
     */
    public SendElectronicDocument createSendElectronicDocument() {
        return new SendElectronicDocument();
    }

    /**
     * Create an instance of {@link SendElectronicDocumentResponse }
     * 
     */
    public SendElectronicDocumentResponse createSendElectronicDocumentResponse() {
        return new SendElectronicDocumentResponse();
    }

    /**
     * Create an instance of {@link SendElectronicDocumentWithAccessCode }
     * 
     */
    public SendElectronicDocumentWithAccessCode createSendElectronicDocumentWithAccessCode() {
        return new SendElectronicDocumentWithAccessCode();
    }

    /**
     * Create an instance of {@link SendElectronicDocumentWithAccessCodeResponse }
     * 
     */
    public SendElectronicDocumentWithAccessCodeResponse createSendElectronicDocumentWithAccessCodeResponse() {
        return new SendElectronicDocumentWithAccessCodeResponse();
    }

    /**
     * Create an instance of {@link SendElectronicDocumentOnBehalfOf }
     * 
     */
    public SendElectronicDocumentOnBehalfOf createSendElectronicDocumentOnBehalfOf() {
        return new SendElectronicDocumentOnBehalfOf();
    }

    /**
     * Create an instance of {@link SendElectronicDocumentOnBehalfOfResponse }
     * 
     */
    public SendElectronicDocumentOnBehalfOfResponse createSendElectronicDocumentOnBehalfOfResponse() {
        return new SendElectronicDocumentOnBehalfOfResponse();
    }

    /**
     * Create an instance of {@link SendMessage }
     * 
     */
    public SendMessage createSendMessage() {
        return new SendMessage();
    }

    /**
     * Create an instance of {@link SendMessageResponse }
     * 
     */
    public SendMessageResponse createSendMessageResponse() {
        return new SendMessageResponse();
    }

    /**
     * Create an instance of {@link SendMessageWithAccessCode }
     * 
     */
    public SendMessageWithAccessCode createSendMessageWithAccessCode() {
        return new SendMessageWithAccessCode();
    }

    /**
     * Create an instance of {@link SendMessageWithAccessCodeResponse }
     * 
     */
    public SendMessageWithAccessCodeResponse createSendMessageWithAccessCodeResponse() {
        return new SendMessageWithAccessCodeResponse();
    }

    /**
     * Create an instance of {@link SendMessageInReplyTo }
     * 
     */
    public SendMessageInReplyTo createSendMessageInReplyTo() {
        return new SendMessageInReplyTo();
    }

    /**
     * Create an instance of {@link SendMessageInReplyToResponse }
     * 
     */
    public SendMessageInReplyToResponse createSendMessageInReplyToResponse() {
        return new SendMessageInReplyToResponse();
    }

    /**
     * Create an instance of {@link SendMessageOnBehalfOf }
     * 
     */
    public SendMessageOnBehalfOf createSendMessageOnBehalfOf() {
        return new SendMessageOnBehalfOf();
    }

    /**
     * Create an instance of {@link SendMessageOnBehalfOfResponse }
     * 
     */
    public SendMessageOnBehalfOfResponse createSendMessageOnBehalfOfResponse() {
        return new SendMessageOnBehalfOfResponse();
    }

    /**
     * Create an instance of {@link GetSentDocumentStatusByRegNum }
     * 
     */
    public GetSentDocumentStatusByRegNum createGetSentDocumentStatusByRegNum() {
        return new GetSentDocumentStatusByRegNum();
    }

    /**
     * Create an instance of {@link GetSentDocumentStatusByRegNumResponse }
     * 
     */
    public GetSentDocumentStatusByRegNumResponse createGetSentDocumentStatusByRegNumResponse() {
        return new GetSentDocumentStatusByRegNumResponse();
    }

    /**
     * Create an instance of {@link GetSentDocumentContentByRegNum }
     * 
     */
    public GetSentDocumentContentByRegNum createGetSentDocumentContentByRegNum() {
        return new GetSentDocumentContentByRegNum();
    }

    /**
     * Create an instance of {@link GetSentDocumentContentByRegNumResponse }
     * 
     */
    public GetSentDocumentContentByRegNumResponse createGetSentDocumentContentByRegNumResponse() {
        return new GetSentDocumentContentByRegNumResponse();
    }

    /**
     * Create an instance of {@link GetSentDocumentsContent }
     * 
     */
    public GetSentDocumentsContent createGetSentDocumentsContent() {
        return new GetSentDocumentsContent();
    }

    /**
     * Create an instance of {@link GetSentDocumentsContentResponse }
     * 
     */
    public GetSentDocumentsContentResponse createGetSentDocumentsContentResponse() {
        return new GetSentDocumentsContentResponse();
    }

    /**
     * Create an instance of {@link GetSentDocumentContent }
     * 
     */
    public GetSentDocumentContent createGetSentDocumentContent() {
        return new GetSentDocumentContent();
    }

    /**
     * Create an instance of {@link GetSentDocumentContentResponse }
     * 
     */
    public GetSentDocumentContentResponse createGetSentDocumentContentResponse() {
        return new GetSentDocumentContentResponse();
    }

    /**
     * Create an instance of {@link GetSentMessageStatus }
     * 
     */
    public GetSentMessageStatus createGetSentMessageStatus() {
        return new GetSentMessageStatus();
    }

    /**
     * Create an instance of {@link GetSentMessageStatusResponse }
     * 
     */
    public GetSentMessageStatusResponse createGetSentMessageStatusResponse() {
        return new GetSentMessageStatusResponse();
    }

    /**
     * Create an instance of {@link GetSentMessagesList }
     * 
     */
    public GetSentMessagesList createGetSentMessagesList() {
        return new GetSentMessagesList();
    }

    /**
     * Create an instance of {@link GetSentMessagesListResponse }
     * 
     */
    public GetSentMessagesListResponse createGetSentMessagesListResponse() {
        return new GetSentMessagesListResponse();
    }

    /**
     * Create an instance of {@link GetSentMessagesListPaged }
     * 
     */
    public GetSentMessagesListPaged createGetSentMessagesListPaged() {
        return new GetSentMessagesListPaged();
    }

    /**
     * Create an instance of {@link GetSentMessagesListPagedResponse }
     * 
     */
    public GetSentMessagesListPagedResponse createGetSentMessagesListPagedResponse() {
        return new GetSentMessagesListPagedResponse();
    }

    /**
     * Create an instance of {@link GetReceivedMessagesList }
     * 
     */
    public GetReceivedMessagesList createGetReceivedMessagesList() {
        return new GetReceivedMessagesList();
    }

    /**
     * Create an instance of {@link GetReceivedMessagesListResponse }
     * 
     */
    public GetReceivedMessagesListResponse createGetReceivedMessagesListResponse() {
        return new GetReceivedMessagesListResponse();
    }

    /**
     * Create an instance of {@link GetReceivedMessagesListPaged }
     * 
     */
    public GetReceivedMessagesListPaged createGetReceivedMessagesListPaged() {
        return new GetReceivedMessagesListPaged();
    }

    /**
     * Create an instance of {@link GetReceivedMessagesListPagedResponse }
     * 
     */
    public GetReceivedMessagesListPagedResponse createGetReceivedMessagesListPagedResponse() {
        return new GetReceivedMessagesListPagedResponse();
    }

    /**
     * Create an instance of {@link GetSentMessageContent }
     * 
     */
    public GetSentMessageContent createGetSentMessageContent() {
        return new GetSentMessageContent();
    }

    /**
     * Create an instance of {@link GetSentMessageContentResponse }
     * 
     */
    public GetSentMessageContentResponse createGetSentMessageContentResponse() {
        return new GetSentMessageContentResponse();
    }

    /**
     * Create an instance of {@link GetReceivedMessageContent }
     * 
     */
    public GetReceivedMessageContent createGetReceivedMessageContent() {
        return new GetReceivedMessageContent();
    }

    /**
     * Create an instance of {@link GetReceivedMessageContentResponse }
     * 
     */
    public GetReceivedMessageContentResponse createGetReceivedMessageContentResponse() {
        return new GetReceivedMessageContentResponse();
    }

    /**
     * Create an instance of {@link CheckPersonHasRegistration }
     * 
     */
    public CheckPersonHasRegistration createCheckPersonHasRegistration() {
        return new CheckPersonHasRegistration();
    }

    /**
     * Create an instance of {@link CheckPersonHasRegistrationResponse }
     * 
     */
    public CheckPersonHasRegistrationResponse createCheckPersonHasRegistrationResponse() {
        return new CheckPersonHasRegistrationResponse();
    }

    /**
     * Create an instance of {@link CheckLegalPersonHasRegistration }
     * 
     */
    public CheckLegalPersonHasRegistration createCheckLegalPersonHasRegistration() {
        return new CheckLegalPersonHasRegistration();
    }

    /**
     * Create an instance of {@link CheckLegalPersonHasRegistrationResponse }
     * 
     */
    public CheckLegalPersonHasRegistrationResponse createCheckLegalPersonHasRegistrationResponse() {
        return new CheckLegalPersonHasRegistrationResponse();
    }

    /**
     * Create an instance of {@link CheckSubjectHasRegistration }
     * 
     */
    public CheckSubjectHasRegistration createCheckSubjectHasRegistration() {
        return new CheckSubjectHasRegistration();
    }

    /**
     * Create an instance of {@link CheckSubjectHasRegistrationResponse }
     * 
     */
    public CheckSubjectHasRegistrationResponse createCheckSubjectHasRegistrationResponse() {
        return new CheckSubjectHasRegistrationResponse();
    }

    /**
     * Create an instance of {@link GetSubjectInfo }
     * 
     */
    public GetSubjectInfo createGetSubjectInfo() {
        return new GetSubjectInfo();
    }

    /**
     * Create an instance of {@link GetSubjectInfoResponse }
     * 
     */
    public GetSubjectInfoResponse createGetSubjectInfoResponse() {
        return new GetSubjectInfoResponse();
    }

    /**
     * Create an instance of {@link GetEDeliveryGeneralStatistics }
     * 
     */
    public GetEDeliveryGeneralStatistics createGetEDeliveryGeneralStatistics() {
        return new GetEDeliveryGeneralStatistics();
    }

    /**
     * Create an instance of {@link GetEDeliveryGeneralStatisticsResponse }
     * 
     */
    public GetEDeliveryGeneralStatisticsResponse createGetEDeliveryGeneralStatisticsResponse() {
        return new GetEDeliveryGeneralStatisticsResponse();
    }

    /**
     * Create an instance of {@link SendMessageOnBehalfToPerson }
     * 
     */
    public SendMessageOnBehalfToPerson createSendMessageOnBehalfToPerson() {
        return new SendMessageOnBehalfToPerson();
    }

    /**
     * Create an instance of {@link SendMessageOnBehalfToPersonResponse }
     * 
     */
    public SendMessageOnBehalfToPersonResponse createSendMessageOnBehalfToPersonResponse() {
        return new SendMessageOnBehalfToPersonResponse();
    }

    /**
     * Create an instance of {@link SendMessageOnBehalfToLegalEntity }
     * 
     */
    public SendMessageOnBehalfToLegalEntity createSendMessageOnBehalfToLegalEntity() {
        return new SendMessageOnBehalfToLegalEntity();
    }

    /**
     * Create an instance of {@link SendMessageOnBehalfToLegalEntityResponse }
     * 
     */
    public SendMessageOnBehalfToLegalEntityResponse createSendMessageOnBehalfToLegalEntityResponse() {
        return new SendMessageOnBehalfToLegalEntityResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcInstitutionInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcInstitutionInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "ArrayOfDcInstitutionInfo")
    public JAXBElement<ArrayOfDcInstitutionInfo> createArrayOfDcInstitutionInfo(ArrayOfDcInstitutionInfo value) {
        return new JAXBElement<ArrayOfDcInstitutionInfo>(_ArrayOfDcInstitutionInfo_QNAME, ArrayOfDcInstitutionInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcInstitutionInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcInstitutionInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcInstitutionInfo")
    public JAXBElement<DcInstitutionInfo> createDcInstitutionInfo(DcInstitutionInfo value) {
        return new JAXBElement<DcInstitutionInfo>(_DcInstitutionInfo_QNAME, DcInstitutionInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcSubjectInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcSubjectInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcSubjectInfo")
    public JAXBElement<DcSubjectInfo> createDcSubjectInfo(DcSubjectInfo value) {
        return new JAXBElement<DcSubjectInfo>(_DcSubjectInfo_QNAME, DcSubjectInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcSubjectPublicInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcSubjectPublicInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcSubjectPublicInfo")
    public JAXBElement<DcSubjectPublicInfo> createDcSubjectPublicInfo(DcSubjectPublicInfo value) {
        return new JAXBElement<DcSubjectPublicInfo>(_DcSubjectPublicInfo_QNAME, DcSubjectPublicInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcAdministrativeActInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcAdministrativeActInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcAdministrativeActInfo")
    public JAXBElement<DcAdministrativeActInfo> createDcAdministrativeActInfo(DcAdministrativeActInfo value) {
        return new JAXBElement<DcAdministrativeActInfo>(_DcAdministrativeActInfo_QNAME, DcAdministrativeActInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcCertificateInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcCertificateInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcCertificateInfo")
    public JAXBElement<DcCertificateInfo> createDcCertificateInfo(DcCertificateInfo value) {
        return new JAXBElement<DcCertificateInfo>(_DcCertificateInfo_QNAME, DcCertificateInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebInstitutionInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WebInstitutionInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "WebInstitutionInfo")
    public JAXBElement<WebInstitutionInfo> createWebInstitutionInfo(WebInstitutionInfo value) {
        return new JAXBElement<WebInstitutionInfo>(_WebInstitutionInfo_QNAME, WebInstitutionInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcLegalPersonInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcLegalPersonInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcLegalPersonInfo")
    public JAXBElement<DcLegalPersonInfo> createDcLegalPersonInfo(DcLegalPersonInfo value) {
        return new JAXBElement<DcLegalPersonInfo>(_DcLegalPersonInfo_QNAME, DcLegalPersonInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcPersonInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcPersonInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcPersonInfo")
    public JAXBElement<DcPersonInfo> createDcPersonInfo(DcPersonInfo value) {
        return new JAXBElement<DcPersonInfo>(_DcPersonInfo_QNAME, DcPersonInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebLegalPersonInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WebLegalPersonInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "WebLegalPersonInfo")
    public JAXBElement<WebLegalPersonInfo> createWebLegalPersonInfo(WebLegalPersonInfo value) {
        return new JAXBElement<WebLegalPersonInfo>(_WebLegalPersonInfo_QNAME, WebLegalPersonInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcTokenVerificationInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcTokenVerificationInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcTokenVerificationInfo")
    public JAXBElement<DcTokenVerificationInfo> createDcTokenVerificationInfo(DcTokenVerificationInfo value) {
        return new JAXBElement<DcTokenVerificationInfo>(_DcTokenVerificationInfo_QNAME, DcTokenVerificationInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcElectronicIdentityInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcElectronicIdentityInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcElectronicIdentityInfo")
    public JAXBElement<DcElectronicIdentityInfo> createDcElectronicIdentityInfo(DcElectronicIdentityInfo value) {
        return new JAXBElement<DcElectronicIdentityInfo>(_DcElectronicIdentityInfo_QNAME, DcElectronicIdentityInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcAddress }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcAddress }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcAddress")
    public JAXBElement<DcAddress> createDcAddress(DcAddress value) {
        return new JAXBElement<DcAddress>(_DcAddress_QNAME, DcAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectPublicInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectPublicInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "ArrayOfDcSubjectPublicInfo")
    public JAXBElement<ArrayOfDcSubjectPublicInfo> createArrayOfDcSubjectPublicInfo(ArrayOfDcSubjectPublicInfo value) {
        return new JAXBElement<ArrayOfDcSubjectPublicInfo>(_ArrayOfDcSubjectPublicInfo_QNAME, ArrayOfDcSubjectPublicInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageWithCodeReceiver }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageWithCodeReceiver }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DcMessageWithCodeReceiver")
    public JAXBElement<DcMessageWithCodeReceiver> createDcMessageWithCodeReceiver(DcMessageWithCodeReceiver value) {
        return new JAXBElement<DcMessageWithCodeReceiver>(_DcMessageWithCodeReceiver_QNAME, DcMessageWithCodeReceiver.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocumentAdditional }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocumentAdditional }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ArrayOfDcDocumentAdditional")
    public JAXBElement<ArrayOfDcDocumentAdditional> createArrayOfDcDocumentAdditional(ArrayOfDcDocumentAdditional value) {
        return new JAXBElement<ArrayOfDcDocumentAdditional>(_ArrayOfDcDocumentAdditional_QNAME, ArrayOfDcDocumentAdditional.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcDocumentAdditional }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcDocumentAdditional }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcDocumentAdditional")
    public JAXBElement<DcDocumentAdditional> createDcDocumentAdditional(DcDocumentAdditional value) {
        return new JAXBElement<DcDocumentAdditional>(_DcDocumentAdditional_QNAME, DcDocumentAdditional.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcDocument")
    public JAXBElement<DcDocument> createDcDocument(DcDocument value) {
        return new JAXBElement<DcDocument>(_DcDocument_QNAME, DcDocument.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSignatureValidationResult }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSignatureValidationResult }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ArrayOfDcSignatureValidationResult")
    public JAXBElement<ArrayOfDcSignatureValidationResult> createArrayOfDcSignatureValidationResult(ArrayOfDcSignatureValidationResult value) {
        return new JAXBElement<ArrayOfDcSignatureValidationResult>(_ArrayOfDcSignatureValidationResult_QNAME, ArrayOfDcSignatureValidationResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcSignatureValidationResult }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcSignatureValidationResult }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcSignatureValidationResult")
    public JAXBElement<DcSignatureValidationResult> createDcSignatureValidationResult(DcSignatureValidationResult value) {
        return new JAXBElement<DcSignatureValidationResult>(_DcSignatureValidationResult_QNAME, DcSignatureValidationResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcChainCertificate }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcChainCertificate }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ArrayOfDcChainCertificate")
    public JAXBElement<ArrayOfDcChainCertificate> createArrayOfDcChainCertificate(ArrayOfDcChainCertificate value) {
        return new JAXBElement<ArrayOfDcChainCertificate>(_ArrayOfDcChainCertificate_QNAME, ArrayOfDcChainCertificate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcChainCertificate }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcChainCertificate }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcChainCertificate")
    public JAXBElement<DcChainCertificate> createDcChainCertificate(DcChainCertificate value) {
        return new JAXBElement<DcChainCertificate>(_DcChainCertificate_QNAME, DcChainCertificate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcTimeStamp }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcTimeStamp }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcTimeStamp")
    public JAXBElement<DcTimeStamp> createDcTimeStamp(DcTimeStamp value) {
        return new JAXBElement<DcTimeStamp>(_DcTimeStamp_QNAME, DcTimeStamp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocument }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocument }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ArrayOfDcDocument")
    public JAXBElement<ArrayOfDcDocument> createArrayOfDcDocument(ArrayOfDcDocument value) {
        return new JAXBElement<ArrayOfDcDocument>(_ArrayOfDcDocument_QNAME, ArrayOfDcDocument.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcMessageDetails")
    public JAXBElement<DcMessageDetails> createDcMessageDetails(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_DcMessageDetails_QNAME, DcMessageDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessage }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessage }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcMessage")
    public JAXBElement<DcMessage> createDcMessage(DcMessage value) {
        return new JAXBElement<DcMessage>(_DcMessage_QNAME, DcMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcLogin")
    public JAXBElement<DcLogin> createDcLogin(DcLogin value) {
        return new JAXBElement<DcLogin>(_DcLogin_QNAME, DcLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcProfile }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcProfile }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ArrayOfDcProfile")
    public JAXBElement<ArrayOfDcProfile> createArrayOfDcProfile(ArrayOfDcProfile value) {
        return new JAXBElement<ArrayOfDcProfile>(_ArrayOfDcProfile_QNAME, ArrayOfDcProfile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcProfile")
    public JAXBElement<DcProfile> createDcProfile(DcProfile value) {
        return new JAXBElement<DcProfile>(_DcProfile_QNAME, DcProfile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcTimeStampMessageContent }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcTimeStampMessageContent }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcTimeStampMessageContent")
    public JAXBElement<DcTimeStampMessageContent> createDcTimeStampMessageContent(DcTimeStampMessageContent value) {
        return new JAXBElement<DcTimeStampMessageContent>(_DcTimeStampMessageContent_QNAME, DcTimeStampMessageContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcMessage }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcMessage }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ArrayOfDcMessage")
    public JAXBElement<ArrayOfDcMessage> createArrayOfDcMessage(ArrayOfDcMessage value) {
        return new JAXBElement<ArrayOfDcMessage>(_ArrayOfDcMessage_QNAME, ArrayOfDcMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcPartialListOfDcMessageHR29GRRX }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcPartialListOfDcMessageHR29GRRX }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcPartialListOfDcMessageHR29gRRX")
    public JAXBElement<DcPartialListOfDcMessageHR29GRRX> createDcPartialListOfDcMessageHR29GRRX(DcPartialListOfDcMessageHR29GRRX value) {
        return new JAXBElement<DcPartialListOfDcMessageHR29GRRX>(_DcPartialListOfDcMessageHR29GRRX_QNAME, DcPartialListOfDcMessageHR29GRRX.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcPersonRegistrationInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcPersonRegistrationInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcPersonRegistrationInfo")
    public JAXBElement<DcPersonRegistrationInfo> createDcPersonRegistrationInfo(DcPersonRegistrationInfo value) {
        return new JAXBElement<DcPersonRegistrationInfo>(_DcPersonRegistrationInfo_QNAME, DcPersonRegistrationInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ArrayOfDcSubjectShortInfo")
    public JAXBElement<ArrayOfDcSubjectShortInfo> createArrayOfDcSubjectShortInfo(ArrayOfDcSubjectShortInfo value) {
        return new JAXBElement<ArrayOfDcSubjectShortInfo>(_ArrayOfDcSubjectShortInfo_QNAME, ArrayOfDcSubjectShortInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcSubjectShortInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcSubjectShortInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcSubjectShortInfo")
    public JAXBElement<DcSubjectShortInfo> createDcSubjectShortInfo(DcSubjectShortInfo value) {
        return new JAXBElement<DcSubjectShortInfo>(_DcSubjectShortInfo_QNAME, DcSubjectShortInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcLegalPersonRegistrationInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcLegalPersonRegistrationInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcLegalPersonRegistrationInfo")
    public JAXBElement<DcLegalPersonRegistrationInfo> createDcLegalPersonRegistrationInfo(DcLegalPersonRegistrationInfo value) {
        return new JAXBElement<DcLegalPersonRegistrationInfo>(_DcLegalPersonRegistrationInfo_QNAME, DcLegalPersonRegistrationInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcSubjectRegistrationInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcSubjectRegistrationInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcSubjectRegistrationInfo")
    public JAXBElement<DcSubjectRegistrationInfo> createDcSubjectRegistrationInfo(DcSubjectRegistrationInfo value) {
        return new JAXBElement<DcSubjectRegistrationInfo>(_DcSubjectRegistrationInfo_QNAME, DcSubjectRegistrationInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcRegisteredSubjectInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcRegisteredSubjectInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcRegisteredSubjectInfo")
    public JAXBElement<DcRegisteredSubjectInfo> createDcRegisteredSubjectInfo(DcRegisteredSubjectInfo value) {
        return new JAXBElement<DcRegisteredSubjectInfo>(_DcRegisteredSubjectInfo_QNAME, DcRegisteredSubjectInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcStatisticsGeneral }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcStatisticsGeneral }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DcStatisticsGeneral")
    public JAXBElement<DcStatisticsGeneral> createDcStatisticsGeneral(DcStatisticsGeneral value) {
        return new JAXBElement<DcStatisticsGeneral>(_DcStatisticsGeneral_QNAME, DcStatisticsGeneral.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfstring")
    public JAXBElement<ArrayOfstring> createArrayOfstring(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_ArrayOfstring_QNAME, ArrayOfstring.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfanyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfanyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfanyType")
    public JAXBElement<ArrayOfanyType> createArrayOfanyType(ArrayOfanyType value) {
        return new JAXBElement<ArrayOfanyType>(_ArrayOfanyType_QNAME, ArrayOfanyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EProfileType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EProfileType }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", name = "eProfileType")
    public JAXBElement<EProfileType> createEProfileType(EProfileType value) {
        return new JAXBElement<EProfileType>(_EProfileType_QNAME, EProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EVerificationInfoType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EVerificationInfoType }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", name = "eVerificationInfoType")
    public JAXBElement<EVerificationInfoType> createEVerificationInfoType(EVerificationInfoType value) {
        return new JAXBElement<EVerificationInfoType>(_EVerificationInfoType_QNAME, EVerificationInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ERevokationResult }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ERevokationResult }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", name = "eRevokationResult")
    public JAXBElement<ERevokationResult> createERevokationResult(ERevokationResult value) {
        return new JAXBElement<ERevokationResult>(_ERevokationResult_QNAME, ERevokationResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EVerificationResult }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EVerificationResult }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", name = "eVerificationResult")
    public JAXBElement<EVerificationResult> createEVerificationResult(EVerificationResult value) {
        return new JAXBElement<EVerificationResult>(_EVerificationResult_QNAME, EVerificationResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ESortColumn }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ESortColumn }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", name = "eSortColumn")
    public JAXBElement<ESortColumn> createESortColumn(ESortColumn value) {
        return new JAXBElement<ESortColumn>(_ESortColumn_QNAME, ESortColumn.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ESortOrder }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ESortOrder }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", name = "eSortOrder")
    public JAXBElement<ESortOrder> createESortOrder(ESortOrder value) {
        return new JAXBElement<ESortOrder>(_ESortOrder_QNAME, ESortOrder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EInstitutionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EInstitutionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums", name = "eInstitutionType")
    public JAXBElement<EInstitutionType> createEInstitutionType(EInstitutionType value) {
        return new JAXBElement<EInstitutionType>(_EInstitutionType_QNAME, EInstitutionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Double }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Float }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Long }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link QName }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Short }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Short }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Long }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcInstitutionInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcInstitutionInfo }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetRegisteredInstitutionsResult", scope = GetRegisteredInstitutionsResponse.class)
    public JAXBElement<ArrayOfDcInstitutionInfo> createGetRegisteredInstitutionsResponseGetRegisteredInstitutionsResult(ArrayOfDcInstitutionInfo value) {
        return new JAXBElement<ArrayOfDcInstitutionInfo>(_GetRegisteredInstitutionsResponseGetRegisteredInstitutionsResult_QNAME, ArrayOfDcInstitutionInfo.class, GetRegisteredInstitutionsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "subject", scope = SendElectronicDocument.class)
    public JAXBElement<String> createSendElectronicDocumentSubject(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentSubject_QNAME, String.class, SendElectronicDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docBytes", scope = SendElectronicDocument.class)
    public JAXBElement<byte[]> createSendElectronicDocumentDocBytes(byte[] value) {
        return new JAXBElement<byte[]>(_SendElectronicDocumentDocBytes_QNAME, byte[].class, SendElectronicDocument.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docNameWithExtension", scope = SendElectronicDocument.class)
    public JAXBElement<String> createSendElectronicDocumentDocNameWithExtension(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentDocNameWithExtension_QNAME, String.class, SendElectronicDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docRegNumber", scope = SendElectronicDocument.class)
    public JAXBElement<String> createSendElectronicDocumentDocRegNumber(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentDocRegNumber_QNAME, String.class, SendElectronicDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverUniqueIdentifier", scope = SendElectronicDocument.class)
    public JAXBElement<String> createSendElectronicDocumentReceiverUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverUniqueIdentifier_QNAME, String.class, SendElectronicDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverPhone", scope = SendElectronicDocument.class)
    public JAXBElement<String> createSendElectronicDocumentReceiverPhone(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverPhone_QNAME, String.class, SendElectronicDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverEmail", scope = SendElectronicDocument.class)
    public JAXBElement<String> createSendElectronicDocumentReceiverEmail(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverEmail_QNAME, String.class, SendElectronicDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendElectronicDocument.class)
    public JAXBElement<String> createSendElectronicDocumentServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendElectronicDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendElectronicDocument.class)
    public JAXBElement<String> createSendElectronicDocumentOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendElectronicDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "subject", scope = SendElectronicDocumentWithAccessCode.class)
    public JAXBElement<String> createSendElectronicDocumentWithAccessCodeSubject(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentSubject_QNAME, String.class, SendElectronicDocumentWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docBytes", scope = SendElectronicDocumentWithAccessCode.class)
    public JAXBElement<byte[]> createSendElectronicDocumentWithAccessCodeDocBytes(byte[] value) {
        return new JAXBElement<byte[]>(_SendElectronicDocumentDocBytes_QNAME, byte[].class, SendElectronicDocumentWithAccessCode.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docNameWithExtension", scope = SendElectronicDocumentWithAccessCode.class)
    public JAXBElement<String> createSendElectronicDocumentWithAccessCodeDocNameWithExtension(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentDocNameWithExtension_QNAME, String.class, SendElectronicDocumentWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docRegNumber", scope = SendElectronicDocumentWithAccessCode.class)
    public JAXBElement<String> createSendElectronicDocumentWithAccessCodeDocRegNumber(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentDocRegNumber_QNAME, String.class, SendElectronicDocumentWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageWithCodeReceiver }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageWithCodeReceiver }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiver", scope = SendElectronicDocumentWithAccessCode.class)
    public JAXBElement<DcMessageWithCodeReceiver> createSendElectronicDocumentWithAccessCodeReceiver(DcMessageWithCodeReceiver value) {
        return new JAXBElement<DcMessageWithCodeReceiver>(_SendElectronicDocumentWithAccessCodeReceiver_QNAME, DcMessageWithCodeReceiver.class, SendElectronicDocumentWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendElectronicDocumentWithAccessCode.class)
    public JAXBElement<String> createSendElectronicDocumentWithAccessCodeServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendElectronicDocumentWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendElectronicDocumentWithAccessCode.class)
    public JAXBElement<String> createSendElectronicDocumentWithAccessCodeOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendElectronicDocumentWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "subject", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfSubject(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentSubject_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docBytes", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<byte[]> createSendElectronicDocumentOnBehalfOfDocBytes(byte[] value) {
        return new JAXBElement<byte[]>(_SendElectronicDocumentDocBytes_QNAME, byte[].class, SendElectronicDocumentOnBehalfOf.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docNameWithExtension", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfDocNameWithExtension(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentDocNameWithExtension_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "docRegNumber", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfDocRegNumber(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentDocRegNumber_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderUniqueIdentifier", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfSenderUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderUniqueIdentifier_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderPhone", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfSenderPhone(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderPhone_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderEmail", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfSenderEmail(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderEmail_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderFirstName", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfSenderFirstName(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderFirstName_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderLastName", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfSenderLastName(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderLastName_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverUniqueIdentifier", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfReceiverUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverUniqueIdentifier_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendElectronicDocumentOnBehalfOf.class)
    public JAXBElement<String> createSendElectronicDocumentOnBehalfOfOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendElectronicDocumentOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "message", scope = SendMessage.class)
    public JAXBElement<DcMessageDetails> createSendMessageMessage(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_SendMessageMessage_QNAME, DcMessageDetails.class, SendMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverUniqueIdentifier", scope = SendMessage.class)
    public JAXBElement<String> createSendMessageReceiverUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverUniqueIdentifier_QNAME, String.class, SendMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverPhone", scope = SendMessage.class)
    public JAXBElement<String> createSendMessageReceiverPhone(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverPhone_QNAME, String.class, SendMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverEmail", scope = SendMessage.class)
    public JAXBElement<String> createSendMessageReceiverEmail(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverEmail_QNAME, String.class, SendMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendMessage.class)
    public JAXBElement<String> createSendMessageServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendMessage.class)
    public JAXBElement<String> createSendMessageOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "message", scope = SendMessageWithAccessCode.class)
    public JAXBElement<DcMessageDetails> createSendMessageWithAccessCodeMessage(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_SendMessageMessage_QNAME, DcMessageDetails.class, SendMessageWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageWithCodeReceiver }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageWithCodeReceiver }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiver", scope = SendMessageWithAccessCode.class)
    public JAXBElement<DcMessageWithCodeReceiver> createSendMessageWithAccessCodeReceiver(DcMessageWithCodeReceiver value) {
        return new JAXBElement<DcMessageWithCodeReceiver>(_SendElectronicDocumentWithAccessCodeReceiver_QNAME, DcMessageWithCodeReceiver.class, SendMessageWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendMessageWithAccessCode.class)
    public JAXBElement<String> createSendMessageWithAccessCodeServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendMessageWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendMessageWithAccessCode.class)
    public JAXBElement<String> createSendMessageWithAccessCodeOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendMessageWithAccessCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "message", scope = SendMessageInReplyTo.class)
    public JAXBElement<DcMessageDetails> createSendMessageInReplyToMessage(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_SendMessageMessage_QNAME, DcMessageDetails.class, SendMessageInReplyTo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendMessageInReplyTo.class)
    public JAXBElement<String> createSendMessageInReplyToServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendMessageInReplyTo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendMessageInReplyTo.class)
    public JAXBElement<String> createSendMessageInReplyToOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendMessageInReplyTo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "message", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<DcMessageDetails> createSendMessageOnBehalfOfMessage(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_SendMessageMessage_QNAME, DcMessageDetails.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderUniqueIdentifier", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<String> createSendMessageOnBehalfOfSenderUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderUniqueIdentifier_QNAME, String.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderPhone", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<String> createSendMessageOnBehalfOfSenderPhone(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderPhone_QNAME, String.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderEmail", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<String> createSendMessageOnBehalfOfSenderEmail(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderEmail_QNAME, String.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderFirstName", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<String> createSendMessageOnBehalfOfSenderFirstName(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderFirstName_QNAME, String.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderLastName", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<String> createSendMessageOnBehalfOfSenderLastName(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderLastName_QNAME, String.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverUniqueIdentifier", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<String> createSendMessageOnBehalfOfReceiverUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverUniqueIdentifier_QNAME, String.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<String> createSendMessageOnBehalfOfServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendMessageOnBehalfOf.class)
    public JAXBElement<String> createSendMessageOnBehalfOfOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendMessageOnBehalfOf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "documentRegistrationNumber", scope = GetSentDocumentStatusByRegNum.class)
    public JAXBElement<String> createGetSentDocumentStatusByRegNumDocumentRegistrationNumber(String value) {
        return new JAXBElement<String>(_GetSentDocumentStatusByRegNumDocumentRegistrationNumber_QNAME, String.class, GetSentDocumentStatusByRegNum.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSentDocumentStatusByRegNum.class)
    public JAXBElement<String> createGetSentDocumentStatusByRegNumOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSentDocumentStatusByRegNum.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSentDocumentStatusByRegNumResult", scope = GetSentDocumentStatusByRegNumResponse.class)
    public JAXBElement<DcMessageDetails> createGetSentDocumentStatusByRegNumResponseGetSentDocumentStatusByRegNumResult(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_GetSentDocumentStatusByRegNumResponseGetSentDocumentStatusByRegNumResult_QNAME, DcMessageDetails.class, GetSentDocumentStatusByRegNumResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "documentRegistrationNumber", scope = GetSentDocumentContentByRegNum.class)
    public JAXBElement<String> createGetSentDocumentContentByRegNumDocumentRegistrationNumber(String value) {
        return new JAXBElement<String>(_GetSentDocumentStatusByRegNumDocumentRegistrationNumber_QNAME, String.class, GetSentDocumentContentByRegNum.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSentDocumentContentByRegNum.class)
    public JAXBElement<String> createGetSentDocumentContentByRegNumOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSentDocumentContentByRegNum.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSentDocumentContentByRegNumResult", scope = GetSentDocumentContentByRegNumResponse.class)
    public JAXBElement<DcDocument> createGetSentDocumentContentByRegNumResponseGetSentDocumentContentByRegNumResult(DcDocument value) {
        return new JAXBElement<DcDocument>(_GetSentDocumentContentByRegNumResponseGetSentDocumentContentByRegNumResult_QNAME, DcDocument.class, GetSentDocumentContentByRegNumResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSentDocumentsContent.class)
    public JAXBElement<String> createGetSentDocumentsContentOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSentDocumentsContent.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocument }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocument }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSentDocumentsContentResult", scope = GetSentDocumentsContentResponse.class)
    public JAXBElement<ArrayOfDcDocument> createGetSentDocumentsContentResponseGetSentDocumentsContentResult(ArrayOfDcDocument value) {
        return new JAXBElement<ArrayOfDcDocument>(_GetSentDocumentsContentResponseGetSentDocumentsContentResult_QNAME, ArrayOfDcDocument.class, GetSentDocumentsContentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSentDocumentContent.class)
    public JAXBElement<String> createGetSentDocumentContentOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSentDocumentContent.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSentDocumentContentResult", scope = GetSentDocumentContentResponse.class)
    public JAXBElement<DcDocument> createGetSentDocumentContentResponseGetSentDocumentContentResult(DcDocument value) {
        return new JAXBElement<DcDocument>(_GetSentDocumentContentResponseGetSentDocumentContentResult_QNAME, DcDocument.class, GetSentDocumentContentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSentMessageStatus.class)
    public JAXBElement<String> createGetSentMessageStatusOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSentMessageStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSentMessageStatusResult", scope = GetSentMessageStatusResponse.class)
    public JAXBElement<DcMessageDetails> createGetSentMessageStatusResponseGetSentMessageStatusResult(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_GetSentMessageStatusResponseGetSentMessageStatusResult_QNAME, DcMessageDetails.class, GetSentMessageStatusResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSentMessagesList.class)
    public JAXBElement<String> createGetSentMessagesListOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSentMessagesList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcMessage }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcMessage }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSentMessagesListResult", scope = GetSentMessagesListResponse.class)
    public JAXBElement<ArrayOfDcMessage> createGetSentMessagesListResponseGetSentMessagesListResult(ArrayOfDcMessage value) {
        return new JAXBElement<ArrayOfDcMessage>(_GetSentMessagesListResponseGetSentMessagesListResult_QNAME, ArrayOfDcMessage.class, GetSentMessagesListResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSentMessagesListPaged.class)
    public JAXBElement<String> createGetSentMessagesListPagedOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSentMessagesListPaged.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcPartialListOfDcMessageHR29GRRX }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcPartialListOfDcMessageHR29GRRX }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSentMessagesListPagedResult", scope = GetSentMessagesListPagedResponse.class)
    public JAXBElement<DcPartialListOfDcMessageHR29GRRX> createGetSentMessagesListPagedResponseGetSentMessagesListPagedResult(DcPartialListOfDcMessageHR29GRRX value) {
        return new JAXBElement<DcPartialListOfDcMessageHR29GRRX>(_GetSentMessagesListPagedResponseGetSentMessagesListPagedResult_QNAME, DcPartialListOfDcMessageHR29GRRX.class, GetSentMessagesListPagedResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetReceivedMessagesList.class)
    public JAXBElement<String> createGetReceivedMessagesListOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetReceivedMessagesList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcMessage }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcMessage }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetReceivedMessagesListResult", scope = GetReceivedMessagesListResponse.class)
    public JAXBElement<ArrayOfDcMessage> createGetReceivedMessagesListResponseGetReceivedMessagesListResult(ArrayOfDcMessage value) {
        return new JAXBElement<ArrayOfDcMessage>(_GetReceivedMessagesListResponseGetReceivedMessagesListResult_QNAME, ArrayOfDcMessage.class, GetReceivedMessagesListResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetReceivedMessagesListPaged.class)
    public JAXBElement<String> createGetReceivedMessagesListPagedOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetReceivedMessagesListPaged.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcPartialListOfDcMessageHR29GRRX }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcPartialListOfDcMessageHR29GRRX }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetReceivedMessagesListPagedResult", scope = GetReceivedMessagesListPagedResponse.class)
    public JAXBElement<DcPartialListOfDcMessageHR29GRRX> createGetReceivedMessagesListPagedResponseGetReceivedMessagesListPagedResult(DcPartialListOfDcMessageHR29GRRX value) {
        return new JAXBElement<DcPartialListOfDcMessageHR29GRRX>(_GetReceivedMessagesListPagedResponseGetReceivedMessagesListPagedResult_QNAME, DcPartialListOfDcMessageHR29GRRX.class, GetReceivedMessagesListPagedResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSentMessageContent.class)
    public JAXBElement<String> createGetSentMessageContentOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSentMessageContent.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSentMessageContentResult", scope = GetSentMessageContentResponse.class)
    public JAXBElement<DcMessageDetails> createGetSentMessageContentResponseGetSentMessageContentResult(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_GetSentMessageContentResponseGetSentMessageContentResult_QNAME, DcMessageDetails.class, GetSentMessageContentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetReceivedMessageContent.class)
    public JAXBElement<String> createGetReceivedMessageContentOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetReceivedMessageContent.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetReceivedMessageContentResult", scope = GetReceivedMessageContentResponse.class)
    public JAXBElement<DcMessageDetails> createGetReceivedMessageContentResponseGetReceivedMessageContentResult(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_GetReceivedMessageContentResponseGetReceivedMessageContentResult_QNAME, DcMessageDetails.class, GetReceivedMessageContentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "personId", scope = CheckPersonHasRegistration.class)
    public JAXBElement<String> createCheckPersonHasRegistrationPersonId(String value) {
        return new JAXBElement<String>(_CheckPersonHasRegistrationPersonId_QNAME, String.class, CheckPersonHasRegistration.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcPersonRegistrationInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcPersonRegistrationInfo }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "CheckPersonHasRegistrationResult", scope = CheckPersonHasRegistrationResponse.class)
    public JAXBElement<DcPersonRegistrationInfo> createCheckPersonHasRegistrationResponseCheckPersonHasRegistrationResult(DcPersonRegistrationInfo value) {
        return new JAXBElement<DcPersonRegistrationInfo>(_CheckPersonHasRegistrationResponseCheckPersonHasRegistrationResult_QNAME, DcPersonRegistrationInfo.class, CheckPersonHasRegistrationResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "eik", scope = CheckLegalPersonHasRegistration.class)
    public JAXBElement<String> createCheckLegalPersonHasRegistrationEik(String value) {
        return new JAXBElement<String>(_CheckLegalPersonHasRegistrationEik_QNAME, String.class, CheckLegalPersonHasRegistration.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcLegalPersonRegistrationInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcLegalPersonRegistrationInfo }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "CheckLegalPersonHasRegistrationResult", scope = CheckLegalPersonHasRegistrationResponse.class)
    public JAXBElement<DcLegalPersonRegistrationInfo> createCheckLegalPersonHasRegistrationResponseCheckLegalPersonHasRegistrationResult(DcLegalPersonRegistrationInfo value) {
        return new JAXBElement<DcLegalPersonRegistrationInfo>(_CheckLegalPersonHasRegistrationResponseCheckLegalPersonHasRegistrationResult_QNAME, DcLegalPersonRegistrationInfo.class, CheckLegalPersonHasRegistrationResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "identificator", scope = CheckSubjectHasRegistration.class)
    public JAXBElement<String> createCheckSubjectHasRegistrationIdentificator(String value) {
        return new JAXBElement<String>(_CheckSubjectHasRegistrationIdentificator_QNAME, String.class, CheckSubjectHasRegistration.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcSubjectRegistrationInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcSubjectRegistrationInfo }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "CheckSubjectHasRegistrationResult", scope = CheckSubjectHasRegistrationResponse.class)
    public JAXBElement<DcSubjectRegistrationInfo> createCheckSubjectHasRegistrationResponseCheckSubjectHasRegistrationResult(DcSubjectRegistrationInfo value) {
        return new JAXBElement<DcSubjectRegistrationInfo>(_CheckSubjectHasRegistrationResponseCheckSubjectHasRegistrationResult_QNAME, DcSubjectRegistrationInfo.class, CheckSubjectHasRegistrationResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = GetSubjectInfo.class)
    public JAXBElement<String> createGetSubjectInfoOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, GetSubjectInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcSubjectInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcSubjectInfo }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetSubjectInfoResult", scope = GetSubjectInfoResponse.class)
    public JAXBElement<DcSubjectInfo> createGetSubjectInfoResponseGetSubjectInfoResult(DcSubjectInfo value) {
        return new JAXBElement<DcSubjectInfo>(_GetSubjectInfoResponseGetSubjectInfoResult_QNAME, DcSubjectInfo.class, GetSubjectInfoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcStatisticsGeneral }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcStatisticsGeneral }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "GetEDeliveryGeneralStatisticsResult", scope = GetEDeliveryGeneralStatisticsResponse.class)
    public JAXBElement<DcStatisticsGeneral> createGetEDeliveryGeneralStatisticsResponseGetEDeliveryGeneralStatisticsResult(DcStatisticsGeneral value) {
        return new JAXBElement<DcStatisticsGeneral>(_GetEDeliveryGeneralStatisticsResponseGetEDeliveryGeneralStatisticsResult_QNAME, DcStatisticsGeneral.class, GetEDeliveryGeneralStatisticsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "message", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<DcMessageDetails> createSendMessageOnBehalfToPersonMessage(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_SendMessageMessage_QNAME, DcMessageDetails.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderUniqueIdentifier", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<String> createSendMessageOnBehalfToPersonSenderUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderUniqueIdentifier_QNAME, String.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverUniqueIdentifier", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<String> createSendMessageOnBehalfToPersonReceiverUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverUniqueIdentifier_QNAME, String.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverPhone", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<String> createSendMessageOnBehalfToPersonReceiverPhone(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverPhone_QNAME, String.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverEmail", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<String> createSendMessageOnBehalfToPersonReceiverEmail(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverEmail_QNAME, String.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverFirstName", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<String> createSendMessageOnBehalfToPersonReceiverFirstName(String value) {
        return new JAXBElement<String>(_SendMessageOnBehalfToPersonReceiverFirstName_QNAME, String.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverLastName", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<String> createSendMessageOnBehalfToPersonReceiverLastName(String value) {
        return new JAXBElement<String>(_SendMessageOnBehalfToPersonReceiverLastName_QNAME, String.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<String> createSendMessageOnBehalfToPersonServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendMessageOnBehalfToPerson.class)
    public JAXBElement<String> createSendMessageOnBehalfToPersonOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendMessageOnBehalfToPerson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "message", scope = SendMessageOnBehalfToLegalEntity.class)
    public JAXBElement<DcMessageDetails> createSendMessageOnBehalfToLegalEntityMessage(DcMessageDetails value) {
        return new JAXBElement<DcMessageDetails>(_SendMessageMessage_QNAME, DcMessageDetails.class, SendMessageOnBehalfToLegalEntity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "senderUniqueIdentifier", scope = SendMessageOnBehalfToLegalEntity.class)
    public JAXBElement<String> createSendMessageOnBehalfToLegalEntitySenderUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOnBehalfOfSenderUniqueIdentifier_QNAME, String.class, SendMessageOnBehalfToLegalEntity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "receiverUniqueIdentifier", scope = SendMessageOnBehalfToLegalEntity.class)
    public JAXBElement<String> createSendMessageOnBehalfToLegalEntityReceiverUniqueIdentifier(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentReceiverUniqueIdentifier_QNAME, String.class, SendMessageOnBehalfToLegalEntity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "serviceOID", scope = SendMessageOnBehalfToLegalEntity.class)
    public JAXBElement<String> createSendMessageOnBehalfToLegalEntityServiceOID(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentServiceOID_QNAME, String.class, SendMessageOnBehalfToLegalEntity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://edelivery.egov.bg/services/integration", name = "operatorEGN", scope = SendMessageOnBehalfToLegalEntity.class)
    public JAXBElement<String> createSendMessageOnBehalfToLegalEntityOperatorEGN(String value) {
        return new JAXBElement<String>(_SendElectronicDocumentOperatorEGN_QNAME, String.class, SendMessageOnBehalfToLegalEntity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "ElectronicSubjectName", scope = DcSubjectPublicInfo.class)
    public JAXBElement<String> createDcSubjectPublicInfoElectronicSubjectName(String value) {
        return new JAXBElement<String>(_DcSubjectPublicInfoElectronicSubjectName_QNAME, String.class, DcSubjectPublicInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Email", scope = DcSubjectPublicInfo.class)
    public JAXBElement<String> createDcSubjectPublicInfoEmail(String value) {
        return new JAXBElement<String>(_DcSubjectPublicInfoEmail_QNAME, String.class, DcSubjectPublicInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "PhoneNumber", scope = DcSubjectPublicInfo.class)
    public JAXBElement<String> createDcSubjectPublicInfoPhoneNumber(String value) {
        return new JAXBElement<String>(_DcSubjectPublicInfoPhoneNumber_QNAME, String.class, DcSubjectPublicInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EInstitutionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EInstitutionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "InstitutionType", scope = DcRegisteredSubjectInfo.class)
    public JAXBElement<EInstitutionType> createDcRegisteredSubjectInfoInstitutionType(EInstitutionType value) {
        return new JAXBElement<EInstitutionType>(_DcRegisteredSubjectInfoInstitutionType_QNAME, EInstitutionType.class, DcRegisteredSubjectInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Identificator", scope = DcSubjectRegistrationInfo.class)
    public JAXBElement<String> createDcSubjectRegistrationInfoIdentificator(String value) {
        return new JAXBElement<String>(_DcSubjectRegistrationInfoIdentificator_QNAME, String.class, DcSubjectRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcRegisteredSubjectInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcRegisteredSubjectInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "SubjectInfo", scope = DcSubjectRegistrationInfo.class)
    public JAXBElement<DcRegisteredSubjectInfo> createDcSubjectRegistrationInfoSubjectInfo(DcRegisteredSubjectInfo value) {
        return new JAXBElement<DcRegisteredSubjectInfo>(_DcSubjectRegistrationInfoSubjectInfo_QNAME, DcRegisteredSubjectInfo.class, DcSubjectRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "EIK", scope = DcLegalPersonRegistrationInfo.class)
    public JAXBElement<String> createDcLegalPersonRegistrationInfoEIK(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoEIK_QNAME, String.class, DcLegalPersonRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Email", scope = DcLegalPersonRegistrationInfo.class)
    public JAXBElement<String> createDcLegalPersonRegistrationInfoEmail(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoEmail_QNAME, String.class, DcLegalPersonRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Name", scope = DcLegalPersonRegistrationInfo.class)
    public JAXBElement<String> createDcLegalPersonRegistrationInfoName(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoName_QNAME, String.class, DcLegalPersonRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Phone", scope = DcLegalPersonRegistrationInfo.class)
    public JAXBElement<String> createDcLegalPersonRegistrationInfoPhone(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoPhone_QNAME, String.class, DcLegalPersonRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ProfilesWithAccess", scope = DcLegalPersonRegistrationInfo.class)
    public JAXBElement<ArrayOfDcSubjectShortInfo> createDcLegalPersonRegistrationInfoProfilesWithAccess(ArrayOfDcSubjectShortInfo value) {
        return new JAXBElement<ArrayOfDcSubjectShortInfo>(_DcLegalPersonRegistrationInfoProfilesWithAccess_QNAME, ArrayOfDcSubjectShortInfo.class, DcLegalPersonRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "EGN", scope = DcSubjectShortInfo.class)
    public JAXBElement<String> createDcSubjectShortInfoEGN(String value) {
        return new JAXBElement<String>(_DcSubjectShortInfoEGN_QNAME, String.class, DcSubjectShortInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "EIK", scope = DcSubjectShortInfo.class)
    public JAXBElement<String> createDcSubjectShortInfoEIK(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoEIK_QNAME, String.class, DcSubjectShortInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Name", scope = DcSubjectShortInfo.class)
    public JAXBElement<String> createDcSubjectShortInfoName(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoName_QNAME, String.class, DcSubjectShortInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "AccessibleProfiles", scope = DcPersonRegistrationInfo.class)
    public JAXBElement<ArrayOfDcSubjectShortInfo> createDcPersonRegistrationInfoAccessibleProfiles(ArrayOfDcSubjectShortInfo value) {
        return new JAXBElement<ArrayOfDcSubjectShortInfo>(_DcPersonRegistrationInfoAccessibleProfiles_QNAME, ArrayOfDcSubjectShortInfo.class, DcPersonRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Name", scope = DcPersonRegistrationInfo.class)
    public JAXBElement<String> createDcPersonRegistrationInfoName(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoName_QNAME, String.class, DcPersonRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "PersonIdentificator", scope = DcPersonRegistrationInfo.class)
    public JAXBElement<String> createDcPersonRegistrationInfoPersonIdentificator(String value) {
        return new JAXBElement<String>(_DcPersonRegistrationInfoPersonIdentificator_QNAME, String.class, DcPersonRegistrationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcMessage }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcMessage }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Items", scope = DcPartialListOfDcMessageHR29GRRX.class)
    public JAXBElement<ArrayOfDcMessage> createDcPartialListOfDcMessageHR29GRRXItems(ArrayOfDcMessage value) {
        return new JAXBElement<ArrayOfDcMessage>(_DcPartialListOfDcMessageHR29GRRXItems_QNAME, ArrayOfDcMessage.class, DcPartialListOfDcMessageHR29GRRX.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Content", scope = DcTimeStampMessageContent.class)
    public JAXBElement<byte[]> createDcTimeStampMessageContentContent(byte[] value) {
        return new JAXBElement<byte[]>(_DcTimeStampMessageContentContent_QNAME, byte[].class, DcTimeStampMessageContent.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ContentType", scope = DcTimeStampMessageContent.class)
    public JAXBElement<String> createDcTimeStampMessageContentContentType(String value) {
        return new JAXBElement<String>(_DcTimeStampMessageContentContentType_QNAME, String.class, DcTimeStampMessageContent.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "FileName", scope = DcTimeStampMessageContent.class)
    public JAXBElement<String> createDcTimeStampMessageContentFileName(String value) {
        return new JAXBElement<String>(_DcTimeStampMessageContentFileName_QNAME, String.class, DcTimeStampMessageContent.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ElectronicSubjectName", scope = DcProfile.class)
    public JAXBElement<String> createDcProfileElectronicSubjectName(String value) {
        return new JAXBElement<String>(_DcProfileElectronicSubjectName_QNAME, String.class, DcProfile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Email", scope = DcProfile.class)
    public JAXBElement<String> createDcProfileEmail(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoEmail_QNAME, String.class, DcProfile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Phone", scope = DcProfile.class)
    public JAXBElement<String> createDcProfilePhone(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoPhone_QNAME, String.class, DcProfile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "CertificateThumbprint", scope = DcLogin.class)
    public JAXBElement<String> createDcLoginCertificateThumbprint(String value) {
        return new JAXBElement<String>(_DcLoginCertificateThumbprint_QNAME, String.class, DcLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ElectronicSubjectName", scope = DcLogin.class)
    public JAXBElement<String> createDcLoginElectronicSubjectName(String value) {
        return new JAXBElement<String>(_DcProfileElectronicSubjectName_QNAME, String.class, DcLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Email", scope = DcLogin.class)
    public JAXBElement<String> createDcLoginEmail(String value) {
        return new JAXBElement<String>(_DcLegalPersonRegistrationInfoEmail_QNAME, String.class, DcLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "PhoneNumber", scope = DcLogin.class)
    public JAXBElement<String> createDcLoginPhoneNumber(String value) {
        return new JAXBElement<String>(_DcLoginPhoneNumber_QNAME, String.class, DcLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcProfile }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcProfile }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Profiles", scope = DcLogin.class)
    public JAXBElement<ArrayOfDcProfile> createDcLoginProfiles(ArrayOfDcProfile value) {
        return new JAXBElement<ArrayOfDcProfile>(_DcLoginProfiles_QNAME, ArrayOfDcProfile.class, DcLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "PushNotificationsUrl", scope = DcLogin.class)
    public JAXBElement<String> createDcLoginPushNotificationsUrl(String value) {
        return new JAXBElement<String>(_DcLoginPushNotificationsUrl_QNAME, String.class, DcLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DateReceived", scope = DcMessage.class)
    public JAXBElement<XMLGregorianCalendar> createDcMessageDateReceived(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DcMessageDateReceived_QNAME, XMLGregorianCalendar.class, DcMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DateSent", scope = DcMessage.class)
    public JAXBElement<XMLGregorianCalendar> createDcMessageDateSent(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DcMessageDateSent_QNAME, XMLGregorianCalendar.class, DcMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ReceiverLogin", scope = DcMessage.class)
    public JAXBElement<DcLogin> createDcMessageReceiverLogin(DcLogin value) {
        return new JAXBElement<DcLogin>(_DcMessageReceiverLogin_QNAME, DcLogin.class, DcMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ReceiverProfile", scope = DcMessage.class)
    public JAXBElement<DcProfile> createDcMessageReceiverProfile(DcProfile value) {
        return new JAXBElement<DcProfile>(_DcMessageReceiverProfile_QNAME, DcProfile.class, DcMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "SenderLogin", scope = DcMessage.class)
    public JAXBElement<DcLogin> createDcMessageSenderLogin(DcLogin value) {
        return new JAXBElement<DcLogin>(_DcMessageSenderLogin_QNAME, DcLogin.class, DcMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "SenderProfile", scope = DcMessage.class)
    public JAXBElement<DcProfile> createDcMessageSenderProfile(DcProfile value) {
        return new JAXBElement<DcProfile>(_DcMessageSenderProfile_QNAME, DcProfile.class, DcMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Title", scope = DcMessage.class)
    public JAXBElement<String> createDcMessageTitle(String value) {
        return new JAXBElement<String>(_DcMessageTitle_QNAME, String.class, DcMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocument }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocument }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "AttachedDocuments", scope = DcMessageDetails.class)
    public JAXBElement<ArrayOfDcDocument> createDcMessageDetailsAttachedDocuments(ArrayOfDcDocument value) {
        return new JAXBElement<ArrayOfDcDocument>(_DcMessageDetailsAttachedDocuments_QNAME, ArrayOfDcDocument.class, DcMessageDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "MessageText", scope = DcMessageDetails.class)
    public JAXBElement<String> createDcMessageDetailsMessageText(String value) {
        return new JAXBElement<String>(_DcMessageDetailsMessageText_QNAME, String.class, DcMessageDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcTimeStampMessageContent }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcTimeStampMessageContent }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "TimeStampContent", scope = DcMessageDetails.class)
    public JAXBElement<DcTimeStampMessageContent> createDcMessageDetailsTimeStampContent(DcTimeStampMessageContent value) {
        return new JAXBElement<DcTimeStampMessageContent>(_DcMessageDetailsTimeStampContent_QNAME, DcTimeStampMessageContent.class, DcMessageDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcTimeStamp }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcTimeStamp }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "TimeStampNRD", scope = DcMessageDetails.class)
    public JAXBElement<DcTimeStamp> createDcMessageDetailsTimeStampNRD(DcTimeStamp value) {
        return new JAXBElement<DcTimeStamp>(_DcMessageDetailsTimeStampNRD_QNAME, DcTimeStamp.class, DcMessageDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcTimeStamp }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcTimeStamp }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "TimeStampNRO", scope = DcMessageDetails.class)
    public JAXBElement<DcTimeStamp> createDcMessageDetailsTimeStampNRO(DcTimeStamp value) {
        return new JAXBElement<DcTimeStamp>(_DcMessageDetailsTimeStampNRO_QNAME, DcTimeStamp.class, DcMessageDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "FileName", scope = DcTimeStamp.class)
    public JAXBElement<String> createDcTimeStampFileName(String value) {
        return new JAXBElement<String>(_DcTimeStampMessageContentFileName_QNAME, String.class, DcTimeStamp.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "TimeStampData", scope = DcTimeStamp.class)
    public JAXBElement<byte[]> createDcTimeStampTimeStampData(byte[] value) {
        return new JAXBElement<byte[]>(_DcTimeStampTimeStampData_QNAME, byte[].class, DcTimeStamp.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Subject", scope = DcChainCertificate.class)
    public JAXBElement<String> createDcChainCertificateSubject(String value) {
        return new JAXBElement<String>(_DcChainCertificateSubject_QNAME, String.class, DcChainCertificate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Content", scope = DcDocument.class)
    public JAXBElement<byte[]> createDcDocumentContent(byte[] value) {
        return new JAXBElement<byte[]>(_DcTimeStampMessageContentContent_QNAME, byte[].class, DcDocument.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ContentEncodingCodePage", scope = DcDocument.class)
    public JAXBElement<Integer> createDcDocumentContentEncodingCodePage(Integer value) {
        return new JAXBElement<Integer>(_DcDocumentContentEncodingCodePage_QNAME, Integer.class, DcDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ContentType", scope = DcDocument.class)
    public JAXBElement<String> createDcDocumentContentType(String value) {
        return new JAXBElement<String>(_DcTimeStampMessageContentContentType_QNAME, String.class, DcDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DocumentName", scope = DcDocument.class)
    public JAXBElement<String> createDcDocumentDocumentName(String value) {
        return new JAXBElement<String>(_DcDocumentDocumentName_QNAME, String.class, DcDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DocumentRegistrationNumber", scope = DcDocument.class)
    public JAXBElement<String> createDcDocumentDocumentRegistrationNumber(String value) {
        return new JAXBElement<String>(_DcDocumentDocumentRegistrationNumber_QNAME, String.class, DcDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSignatureValidationResult }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSignatureValidationResult }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "SignaturesInfo", scope = DcDocument.class)
    public JAXBElement<ArrayOfDcSignatureValidationResult> createDcDocumentSignaturesInfo(ArrayOfDcSignatureValidationResult value) {
        return new JAXBElement<ArrayOfDcSignatureValidationResult>(_DcDocumentSignaturesInfo_QNAME, ArrayOfDcSignatureValidationResult.class, DcDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcTimeStamp }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcTimeStamp }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "TimeStamp", scope = DcDocument.class)
    public JAXBElement<DcTimeStamp> createDcDocumentTimeStamp(DcTimeStamp value) {
        return new JAXBElement<DcTimeStamp>(_DcDocumentTimeStamp_QNAME, DcTimeStamp.class, DcDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "Content", scope = DcDocumentAdditional.class)
    public JAXBElement<byte[]> createDcDocumentAdditionalContent(byte[] value) {
        return new JAXBElement<byte[]>(_DcTimeStampMessageContentContent_QNAME, byte[].class, DcDocumentAdditional.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ContentEncodingCodePage", scope = DcDocumentAdditional.class)
    public JAXBElement<Integer> createDcDocumentAdditionalContentEncodingCodePage(Integer value) {
        return new JAXBElement<Integer>(_DcDocumentContentEncodingCodePage_QNAME, Integer.class, DcDocumentAdditional.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "ContentType", scope = DcDocumentAdditional.class)
    public JAXBElement<String> createDcDocumentAdditionalContentType(String value) {
        return new JAXBElement<String>(_DcTimeStampMessageContentContentType_QNAME, String.class, DcDocumentAdditional.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "CreatedBy", scope = DcDocumentAdditional.class)
    public JAXBElement<String> createDcDocumentAdditionalCreatedBy(String value) {
        return new JAXBElement<String>(_DcDocumentAdditionalCreatedBy_QNAME, String.class, DcDocumentAdditional.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DocumentDescription", scope = DcDocumentAdditional.class)
    public JAXBElement<String> createDcDocumentAdditionalDocumentDescription(String value) {
        return new JAXBElement<String>(_DcDocumentAdditionalDocumentDescription_QNAME, String.class, DcDocumentAdditional.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", name = "DocumentName", scope = DcDocumentAdditional.class)
    public JAXBElement<String> createDcDocumentAdditionalDocumentName(String value) {
        return new JAXBElement<String>(_DcDocumentDocumentName_QNAME, String.class, DcDocumentAdditional.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "EGNorLNCH", scope = DcMessageWithCodeReceiver.class)
    public JAXBElement<String> createDcMessageWithCodeReceiverEGNorLNCH(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverEGNorLNCH_QNAME, String.class, DcMessageWithCodeReceiver.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Email", scope = DcMessageWithCodeReceiver.class)
    public JAXBElement<String> createDcMessageWithCodeReceiverEmail(String value) {
        return new JAXBElement<String>(_DcSubjectPublicInfoEmail_QNAME, String.class, DcMessageWithCodeReceiver.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "FirstName", scope = DcMessageWithCodeReceiver.class)
    public JAXBElement<String> createDcMessageWithCodeReceiverFirstName(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverFirstName_QNAME, String.class, DcMessageWithCodeReceiver.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "LastName", scope = DcMessageWithCodeReceiver.class)
    public JAXBElement<String> createDcMessageWithCodeReceiverLastName(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverLastName_QNAME, String.class, DcMessageWithCodeReceiver.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "MiddleName", scope = DcMessageWithCodeReceiver.class)
    public JAXBElement<String> createDcMessageWithCodeReceiverMiddleName(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverMiddleName_QNAME, String.class, DcMessageWithCodeReceiver.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Phone", scope = DcMessageWithCodeReceiver.class)
    public JAXBElement<String> createDcMessageWithCodeReceiverPhone(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverPhone_QNAME, String.class, DcMessageWithCodeReceiver.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Address", scope = DcAddress.class)
    public JAXBElement<String> createDcAddressAddress(String value) {
        return new JAXBElement<String>(_DcAddressAddress_QNAME, String.class, DcAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "City", scope = DcAddress.class)
    public JAXBElement<String> createDcAddressCity(String value) {
        return new JAXBElement<String>(_DcAddressCity_QNAME, String.class, DcAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "CountryIso2", scope = DcAddress.class)
    public JAXBElement<String> createDcAddressCountryIso2(String value) {
        return new JAXBElement<String>(_DcAddressCountryIso2_QNAME, String.class, DcAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "State", scope = DcAddress.class)
    public JAXBElement<String> createDcAddressState(String value) {
        return new JAXBElement<String>(_DcAddressState_QNAME, String.class, DcAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "ZipCode", scope = DcAddress.class)
    public JAXBElement<String> createDcAddressZipCode(String value) {
        return new JAXBElement<String>(_DcAddressZipCode_QNAME, String.class, DcAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Address", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoAddress(String value) {
        return new JAXBElement<String>(_DcAddressAddress_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DateOfBirth", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<XMLGregorianCalendar> createDcElectronicIdentityInfoDateOfBirth(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DcElectronicIdentityInfoDateOfBirth_QNAME, XMLGregorianCalendar.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "EGN", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoEGN(String value) {
        return new JAXBElement<String>(_DcElectronicIdentityInfoEGN_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "FamilyName", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoFamilyName(String value) {
        return new JAXBElement<String>(_DcElectronicIdentityInfoFamilyName_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "FamilyNameLat", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoFamilyNameLat(String value) {
        return new JAXBElement<String>(_DcElectronicIdentityInfoFamilyNameLat_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "GivenName", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoGivenName(String value) {
        return new JAXBElement<String>(_DcElectronicIdentityInfoGivenName_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "GivenNameLat", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoGivenNameLat(String value) {
        return new JAXBElement<String>(_DcElectronicIdentityInfoGivenNameLat_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "MiddleName", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoMiddleName(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverMiddleName_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "MiddleNameLat", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoMiddleNameLat(String value) {
        return new JAXBElement<String>(_DcElectronicIdentityInfoMiddleNameLat_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "PID", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoPID(String value) {
        return new JAXBElement<String>(_DcElectronicIdentityInfoPID_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "PhoneNumber", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoPhoneNumber(String value) {
        return new JAXBElement<String>(_DcSubjectPublicInfoPhoneNumber_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Spin", scope = DcElectronicIdentityInfo.class)
    public JAXBElement<String> createDcElectronicIdentityInfoSpin(String value) {
        return new JAXBElement<String>(_DcElectronicIdentityInfoSpin_QNAME, String.class, DcElectronicIdentityInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Token", scope = DcTokenVerificationInfo.class)
    public JAXBElement<String> createDcTokenVerificationInfoToken(String value) {
        return new JAXBElement<String>(_DcTokenVerificationInfoToken_QNAME, String.class, DcTokenVerificationInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcAddress }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcAddress }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Address", scope = DcSubjectInfo.class)
    public JAXBElement<DcAddress> createDcSubjectInfoAddress(DcAddress value) {
        return new JAXBElement<DcAddress>(_DcAddressAddress_QNAME, DcAddress.class, DcSubjectInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DateCreated", scope = DcSubjectInfo.class)
    public JAXBElement<XMLGregorianCalendar> createDcSubjectInfoDateCreated(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DcSubjectInfoDateCreated_QNAME, XMLGregorianCalendar.class, DcSubjectInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "UniqueSubjectIdentifier", scope = DcSubjectInfo.class)
    public JAXBElement<String> createDcSubjectInfoUniqueSubjectIdentifier(String value) {
        return new JAXBElement<String>(_DcSubjectInfoUniqueSubjectIdentifier_QNAME, String.class, DcSubjectInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfanyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfanyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "VerificationInfo", scope = DcSubjectInfo.class)
    public JAXBElement<ArrayOfanyType> createDcSubjectInfoVerificationInfo(ArrayOfanyType value) {
        return new JAXBElement<ArrayOfanyType>(_DcSubjectInfoVerificationInfo_QNAME, ArrayOfanyType.class, DcSubjectInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "CompanyName", scope = DcLegalPersonInfo.class)
    public JAXBElement<String> createDcLegalPersonInfoCompanyName(String value) {
        return new JAXBElement<String>(_DcLegalPersonInfoCompanyName_QNAME, String.class, DcLegalPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DateOutOfForce", scope = DcLegalPersonInfo.class)
    public JAXBElement<XMLGregorianCalendar> createDcLegalPersonInfoDateOutOfForce(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DcLegalPersonInfoDateOutOfForce_QNAME, XMLGregorianCalendar.class, DcLegalPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcPersonInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcPersonInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "RegisteredBy", scope = DcLegalPersonInfo.class)
    public JAXBElement<DcPersonInfo> createDcLegalPersonInfoRegisteredBy(DcPersonInfo value) {
        return new JAXBElement<DcPersonInfo>(_DcLegalPersonInfoRegisteredBy_QNAME, DcPersonInfo.class, DcLegalPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DateDeleted", scope = WebLegalPersonInfo.class)
    public JAXBElement<XMLGregorianCalendar> createWebLegalPersonInfoDateDeleted(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_WebLegalPersonInfoDateDeleted_QNAME, XMLGregorianCalendar.class, WebLegalPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocument }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocument }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "RegistrationDcouments", scope = WebLegalPersonInfo.class)
    public JAXBElement<ArrayOfDcDocument> createWebLegalPersonInfoRegistrationDcouments(ArrayOfDcDocument value) {
        return new JAXBElement<ArrayOfDcDocument>(_WebLegalPersonInfoRegistrationDcouments_QNAME, ArrayOfDcDocument.class, WebLegalPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DateOfDeath", scope = DcPersonInfo.class)
    public JAXBElement<XMLGregorianCalendar> createDcPersonInfoDateOfDeath(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DcPersonInfoDateOfDeath_QNAME, XMLGregorianCalendar.class, DcPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "FirstName", scope = DcPersonInfo.class)
    public JAXBElement<String> createDcPersonInfoFirstName(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverFirstName_QNAME, String.class, DcPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "LastName", scope = DcPersonInfo.class)
    public JAXBElement<String> createDcPersonInfoLastName(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverLastName_QNAME, String.class, DcPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "MiddleName", scope = DcPersonInfo.class)
    public JAXBElement<String> createDcPersonInfoMiddleName(String value) {
        return new JAXBElement<String>(_DcMessageWithCodeReceiverMiddleName_QNAME, String.class, DcPersonInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcSubjectPublicInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcSubjectPublicInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "HeadInstitution", scope = DcInstitutionInfo.class)
    public JAXBElement<DcSubjectPublicInfo> createDcInstitutionInfoHeadInstitution(DcSubjectPublicInfo value) {
        return new JAXBElement<DcSubjectPublicInfo>(_DcInstitutionInfoHeadInstitution_QNAME, DcSubjectPublicInfo.class, DcInstitutionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Name", scope = DcInstitutionInfo.class)
    public JAXBElement<String> createDcInstitutionInfoName(String value) {
        return new JAXBElement<String>(_DcInstitutionInfoName_QNAME, String.class, DcInstitutionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectPublicInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectPublicInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "SubInstitutions", scope = DcInstitutionInfo.class)
    public JAXBElement<ArrayOfDcSubjectPublicInfo> createDcInstitutionInfoSubInstitutions(ArrayOfDcSubjectPublicInfo value) {
        return new JAXBElement<ArrayOfDcSubjectPublicInfo>(_DcInstitutionInfoSubInstitutions_QNAME, ArrayOfDcSubjectPublicInfo.class, DcInstitutionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocumentAdditional }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfDcDocumentAdditional }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "AdditionalDcouments", scope = WebInstitutionInfo.class)
    public JAXBElement<ArrayOfDcDocumentAdditional> createWebInstitutionInfoAdditionalDcouments(ArrayOfDcDocumentAdditional value) {
        return new JAXBElement<ArrayOfDcDocumentAdditional>(_WebInstitutionInfoAdditionalDcouments_QNAME, ArrayOfDcDocumentAdditional.class, WebInstitutionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "DateDeleted", scope = WebInstitutionInfo.class)
    public JAXBElement<XMLGregorianCalendar> createWebInstitutionInfoDateDeleted(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_WebLegalPersonInfoDateDeleted_QNAME, XMLGregorianCalendar.class, WebInstitutionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "RegistrationDocument", scope = WebInstitutionInfo.class)
    public JAXBElement<DcDocument> createWebInstitutionInfoRegistrationDocument(DcDocument value) {
        return new JAXBElement<DcDocument>(_WebInstitutionInfoRegistrationDocument_QNAME, DcDocument.class, WebInstitutionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Issuer", scope = DcCertificateInfo.class)
    public JAXBElement<String> createDcCertificateInfoIssuer(String value) {
        return new JAXBElement<String>(_DcCertificateInfoIssuer_QNAME, String.class, DcCertificateInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "Subject", scope = DcCertificateInfo.class)
    public JAXBElement<String> createDcCertificateInfoSubject(String value) {
        return new JAXBElement<String>(_DcCertificateInfoSubject_QNAME, String.class, DcCertificateInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "ActNumber", scope = DcAdministrativeActInfo.class)
    public JAXBElement<String> createDcAdministrativeActInfoActNumber(String value) {
        return new JAXBElement<String>(_DcAdministrativeActInfoActNumber_QNAME, String.class, DcAdministrativeActInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", name = "CreatedByInstitution", scope = DcAdministrativeActInfo.class)
    public JAXBElement<String> createDcAdministrativeActInfoCreatedByInstitution(String value) {
        return new JAXBElement<String>(_DcAdministrativeActInfoCreatedByInstitution_QNAME, String.class, DcAdministrativeActInfo.class, value);
    }

}
