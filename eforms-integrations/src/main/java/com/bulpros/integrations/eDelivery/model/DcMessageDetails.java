
package com.bulpros.integrations.eDelivery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for DcMessageDetails complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcMessageDetails"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcMessage"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="AttachedDocuments" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}ArrayOfDcDocument" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="MessageText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="TimeStampContent" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcTimeStampMessageContent" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="TimeStampNRD" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcTimeStamp" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="TimeStampNRO" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcTimeStamp" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcMessageDetails", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
        "attachedDocuments",
        "messageText",
        "timeStampContent",
        "timeStampNRD",
        "timeStampNRO"
})
public class DcMessageDetails
        extends DcMessage {

    @XmlElement(name = "AttachedDocuments")
    @JsonProperty("AttachedDocuments")
    protected ArrayOfDcDocument attachedDocuments;
    @XmlElement(name = "MessageText")
    @JsonProperty("MessageText")
    protected String messageText;
    @XmlElement(name = "TimeStampContent")
    @JsonProperty("TimeStampContent")
    protected DcTimeStampMessageContent timeStampContent;
    @XmlElement(name = "TimeStampNRD")
    @JsonProperty("TimeStampNRD")
    protected DcTimeStamp timeStampNRD;
    @XmlElement(name = "TimeStampNRO")
    @JsonProperty("TimeStampNRO")
    protected DcTimeStamp timeStampNRO;


    public ArrayOfDcDocument getAttachedDocuments() {
        return attachedDocuments;
    }

    public void setAttachedDocuments(ArrayOfDcDocument value) {
        this.attachedDocuments = value;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String value) {
        this.messageText = value;
    }

    public DcTimeStampMessageContent getTimeStampContent() {
        return timeStampContent;
    }

    public void setTimeStampContent(DcTimeStampMessageContent value) {
        this.timeStampContent = value;
    }

    public DcTimeStamp getTimeStampNRD() {
        return timeStampNRD;
    }

    public void setTimeStampNRD(DcTimeStamp value) {
        this.timeStampNRD = value;
    }

    public DcTimeStamp getTimeStampNRO() {
        return timeStampNRO;
    }

    public void setTimeStampNRO(DcTimeStamp value) {
        this.timeStampNRO = value;
    }

}
