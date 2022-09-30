
package com.bulpros.integrations.regix.model.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bulpros.integrations.regix.model.client package. 
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

    private final static QName _DataContainerMatrix_QNAME = new QName("http://egov.bg/RegiX/SignedData", "Matrix");
    private final static QName _ServiceResultDataData_QNAME = new QName("http://egov.bg/RegiX/SignedData", "Data");
    private final static QName _ServiceResultDataError_QNAME = new QName("http://egov.bg/RegiX/SignedData", "Error");
    private final static QName _CallContextEmployeeIdentifier_QNAME = new QName("http://egov.bg/RegiX/SignedData", "EmployeeIdentifier");
    private final static QName _CallContextEmployeeNames_QNAME = new QName("http://egov.bg/RegiX/SignedData", "EmployeeNames");
    private final static QName _CallContextEmployeeAditionalIdentifier_QNAME = new QName("http://egov.bg/RegiX/SignedData", "EmployeeAditionalIdentifier");
    private final static QName _CallContextEmployeePosition_QNAME = new QName("http://egov.bg/RegiX/SignedData", "EmployeePosition");
    private final static QName _CallContextAdministrationOId_QNAME = new QName("http://egov.bg/RegiX/SignedData", "AdministrationOId");
    private final static QName _CallContextAdministrationName_QNAME = new QName("http://egov.bg/RegiX/SignedData", "AdministrationName");
    private final static QName _CallContextResponsiblePersonIdentifier_QNAME = new QName("http://egov.bg/RegiX/SignedData", "ResponsiblePersonIdentifier");
    private final static QName _CallContextRemark_QNAME = new QName("http://egov.bg/RegiX/SignedData", "Remark");
    private final static QName _ServiceRequestDataEIDToken_QNAME = new QName("http://egov.bg/RegiX/SignedData", "EIDToken");
    private final static QName _ServiceRequestDataCallbackURL_QNAME = new QName("http://egov.bg/RegiX/SignedData", "CallbackURL");
    private final static QName _ServiceRequestDataEmployeeEGN_QNAME = new QName("http://egov.bg/RegiX/SignedData", "EmployeeEGN");
    private final static QName _ServiceRequestDataCitizenEGN_QNAME = new QName("http://egov.bg/RegiX/SignedData", "CitizenEGN");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bulpros.integrations.regix.model.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataContainer }
     * 
     */
    public DataContainer createDataContainer() {
        return new DataContainer();
    }

    /**
     * Create an instance of {@link ServiceRequestData }
     * 
     */
    public ServiceRequestData createServiceRequestData() {
        return new ServiceRequestData();
    }

    /**
     * Create an instance of {@link Execute }
     * 
     */
    public Execute createExecute() {
        return new Execute();
    }

    /**
     * Create an instance of {@link RequestWrapper }
     * 
     */
    public RequestWrapper createRequestWrapper() {
        return new RequestWrapper();
    }

    /**
     * Create an instance of {@link ExecuteResponse }
     * 
     */
    public ExecuteResponse createExecuteResponse() {
        return new ExecuteResponse();
    }

    /**
     * Create an instance of {@link ResultWrapper }
     * 
     */
    public ResultWrapper createResultWrapper() {
        return new ResultWrapper();
    }

    /**
     * Create an instance of {@link CheckResult }
     * 
     */
    public CheckResult createCheckResult() {
        return new CheckResult();
    }

    /**
     * Create an instance of {@link ServiceCheckResultWrapper }
     * 
     */
    public ServiceCheckResultWrapper createServiceCheckResultWrapper() {
        return new ServiceCheckResultWrapper();
    }

    /**
     * Create an instance of {@link CheckResultResponse }
     * 
     */
    public CheckResultResponse createCheckResultResponse() {
        return new CheckResultResponse();
    }

    /**
     * Create an instance of {@link AcknowledgeResultReceived }
     * 
     */
    public AcknowledgeResultReceived createAcknowledgeResultReceived() {
        return new AcknowledgeResultReceived();
    }

    /**
     * Create an instance of {@link AcknowledgeResultReceivedResponse }
     * 
     */
    public AcknowledgeResultReceivedResponse createAcknowledgeResultReceivedResponse() {
        return new AcknowledgeResultReceivedResponse();
    }

    /**
     * Create an instance of {@link ExecuteCallback }
     * 
     */
    public ExecuteCallback createExecuteCallback() {
        return new ExecuteCallback();
    }

    /**
     * Create an instance of {@link ExecuteCallbackResponse }
     * 
     */
    public ExecuteCallbackResponse createExecuteCallbackResponse() {
        return new ExecuteCallbackResponse();
    }

    /**
     * Create an instance of {@link CallContext }
     * 
     */
    public CallContext createCallContext() {
        return new CallContext();
    }

    /**
     * Create an instance of {@link ServiceResultData }
     * 
     */
    public ServiceResultData createServiceResultData() {
        return new ServiceResultData();
    }

    /**
     * Create an instance of {@link RequestContainer }
     * 
     */
    public RequestContainer createRequestContainer() {
        return new RequestContainer();
    }

    /**
     * Create an instance of {@link ResponseContainer }
     * 
     */
    public ResponseContainer createResponseContainer() {
        return new ResponseContainer();
    }

    /**
     * Create an instance of {@link AccessMatrixType }
     * 
     */
    public AccessMatrixType createAccessMatrixType() {
        return new AccessMatrixType();
    }

    /**
     * Create an instance of {@link ArrayOfAMPropertyType }
     * 
     */
    public ArrayOfAMPropertyType createArrayOfAMPropertyType() {
        return new ArrayOfAMPropertyType();
    }

    /**
     * Create an instance of {@link AMPropertyType }
     * 
     */
    public AMPropertyType createAMPropertyType() {
        return new AMPropertyType();
    }

    /**
     * Create an instance of {@link ServiceCheckResultArgument }
     * 
     */
    public ServiceCheckResultArgument createServiceCheckResultArgument() {
        return new ServiceCheckResultArgument();
    }

    /**
     * Create an instance of {@link DataContainer.Matrix }
     * 
     */
    public DataContainer.Matrix createDataContainerMatrix() {
        return new DataContainer.Matrix();
    }

    /**
     * Create an instance of {@link ServiceRequestData.Argument }
     * 
     */
    public ServiceRequestData.Argument createServiceRequestDataArgument() {
        return new ServiceRequestData.Argument();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataContainer.Matrix }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataContainer.Matrix }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "Matrix", scope = DataContainer.class)
    public JAXBElement<DataContainer.Matrix> createDataContainerMatrix(DataContainer.Matrix value) {
        return new JAXBElement<DataContainer.Matrix>(_DataContainerMatrix_QNAME, DataContainer.Matrix.class, DataContainer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataContainer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataContainer }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "Data", scope = ServiceResultData.class)
    public JAXBElement<DataContainer> createServiceResultDataData(DataContainer value) {
        return new JAXBElement<DataContainer>(_ServiceResultDataData_QNAME, DataContainer.class, ServiceResultData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "Error", scope = ServiceResultData.class)
    public JAXBElement<String> createServiceResultDataError(String value) {
        return new JAXBElement<String>(_ServiceResultDataError_QNAME, String.class, ServiceResultData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "EmployeeIdentifier", scope = CallContext.class)
    public JAXBElement<String> createCallContextEmployeeIdentifier(String value) {
        return new JAXBElement<String>(_CallContextEmployeeIdentifier_QNAME, String.class, CallContext.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "EmployeeNames", scope = CallContext.class)
    public JAXBElement<String> createCallContextEmployeeNames(String value) {
        return new JAXBElement<String>(_CallContextEmployeeNames_QNAME, String.class, CallContext.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "EmployeeAditionalIdentifier", scope = CallContext.class)
    public JAXBElement<String> createCallContextEmployeeAditionalIdentifier(String value) {
        return new JAXBElement<String>(_CallContextEmployeeAditionalIdentifier_QNAME, String.class, CallContext.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "EmployeePosition", scope = CallContext.class)
    public JAXBElement<String> createCallContextEmployeePosition(String value) {
        return new JAXBElement<String>(_CallContextEmployeePosition_QNAME, String.class, CallContext.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "AdministrationOId", scope = CallContext.class)
    public JAXBElement<String> createCallContextAdministrationOId(String value) {
        return new JAXBElement<String>(_CallContextAdministrationOId_QNAME, String.class, CallContext.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "AdministrationName", scope = CallContext.class)
    public JAXBElement<String> createCallContextAdministrationName(String value) {
        return new JAXBElement<String>(_CallContextAdministrationName_QNAME, String.class, CallContext.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "ResponsiblePersonIdentifier", scope = CallContext.class)
    public JAXBElement<String> createCallContextResponsiblePersonIdentifier(String value) {
        return new JAXBElement<String>(_CallContextResponsiblePersonIdentifier_QNAME, String.class, CallContext.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "Remark", scope = CallContext.class)
    public JAXBElement<String> createCallContextRemark(String value) {
        return new JAXBElement<String>(_CallContextRemark_QNAME, String.class, CallContext.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "EIDToken", scope = ServiceRequestData.class)
    public JAXBElement<String> createServiceRequestDataEIDToken(String value) {
        return new JAXBElement<String>(_ServiceRequestDataEIDToken_QNAME, String.class, ServiceRequestData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "CallbackURL", scope = ServiceRequestData.class)
    public JAXBElement<String> createServiceRequestDataCallbackURL(String value) {
        return new JAXBElement<String>(_ServiceRequestDataCallbackURL_QNAME, String.class, ServiceRequestData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "EmployeeEGN", scope = ServiceRequestData.class)
    public JAXBElement<String> createServiceRequestDataEmployeeEGN(String value) {
        return new JAXBElement<String>(_ServiceRequestDataEmployeeEGN_QNAME, String.class, ServiceRequestData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://egov.bg/RegiX/SignedData", name = "CitizenEGN", scope = ServiceRequestData.class)
    public JAXBElement<String> createServiceRequestDataCitizenEGN(String value) {
        return new JAXBElement<String>(_ServiceRequestDataCitizenEGN_QNAME, String.class, ServiceRequestData.class, value);
    }

}
