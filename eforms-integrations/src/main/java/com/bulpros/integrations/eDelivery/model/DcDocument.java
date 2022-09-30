
package com.bulpros.integrations.eDelivery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for DcDocument complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcDocument"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ContentEncodingCodePage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ContentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DocumentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DocumentRegistrationNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SignaturesInfo" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}ArrayOfDcSignatureValidationResult" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="TimeStamp" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcTimeStamp" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcDocument", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
        "content",
        "contentEncodingCodePage",
        "contentType",
        "documentName",
        "documentRegistrationNumber",
        "id",
        "signaturesInfo",
        "timeStamp"
})
public class DcDocument {

    @XmlElement(name = "Content")
    @JsonProperty("Content")
    protected byte[] content;
    @XmlElement(name = "ContentEncodingCodePage")
    @JsonProperty("ContentEncodingCodePage")
    protected Integer contentEncodingCodePage;
    @XmlElement(name = "ContentType")
    @JsonProperty("ContentType")
    protected String contentType;
    @XmlElement(name = "DocumentName")
    @JsonProperty("DocumentName")
    protected String documentName;
    @XmlElement(name = "DocumentRegistrationNumber")
    @JsonProperty("DocumentRegistrationNumber")
    protected String documentRegistrationNumber;
    @XmlElement(name = "Id")
    @JsonProperty("Id")
    protected Integer id;
    @XmlElement(name = "SignaturesInfo")
    @JsonProperty("SignaturesInfo")
    protected ArrayOfDcSignatureValidationResult signaturesInfo;
    @XmlElement(name = "TimeStamp")
    @JsonProperty("TimeStamp")
    protected DcTimeStamp timeStamp;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] value) {
        this.content = value;
    }

    public Integer getContentEncodingCodePage() {
        return contentEncodingCodePage;
    }

    public void setContentEncodingCodePage(Integer value) {
        this.contentEncodingCodePage = value;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String value) {
        this.contentType = value;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String value) {
        this.documentName = value;
    }

    public String getDocumentRegistrationNumber() {
        return documentRegistrationNumber;
    }

    public void setDocumentRegistrationNumber(String value) {
        this.documentRegistrationNumber = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer value) {
        this.id = value;
    }

    public ArrayOfDcSignatureValidationResult getSignaturesInfo() {
        return signaturesInfo;
    }

    public void setSignaturesInfo(ArrayOfDcSignatureValidationResult value) {
        this.signaturesInfo = value;
    }

    public DcTimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DcTimeStamp value) {
        this.timeStamp = value;
    }

}
