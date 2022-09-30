package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.camunda.model.EDeliveryFilesPackageDetails;
import com.bulpros.eforms.processengine.camunda.model.enums.EDeliveryFilesPackageStatus;
import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.model.enums.GenerateFilesParticipantType;
import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.minio.model.MinioFile;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import com.bulpros.eforms.processengine.util.CommonUtils;
import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.service.FormsService;
import com.bulpros.formio.service.PdfService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.spin.Spin;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateFilesPackageServiceImpl implements GenerateFilesPackageService {


    @Value("${com.bulpros.process.eDelivery.xml.service.request.template}")
    private ClassPathResource serviceRequestTemplate;

    @Value("${com.bulpros.process.eDelivery.xml.service.request.optional.template}")
    private ClassPathResource serviceRequestOptionalTemplate;

    @Value("${com.bulpros.process.eDelivery.xml.service.request.response.template}")
    private ClassPathResource serviceRequestResponseTemplate;

    @Value("${com.bulpros.process.eDelivery.xml.service.request.specific.content.tags}")
    private String specificContentTags;

    @Value("${com.bulpros.process.eDelivery.xml.service.request.validation.schema}")
    private ClassPathResource validationSchema;

    private final FormsService formsService;
    private final PdfService pdfService;
    private final MinioService minioService;
    private final RuntimeService runtimeService;
    private final ConfigurationProperties processConfProperties;
    private final Configuration jsonPathConfiguration;

    @Async("generateFilesPackageTaskExecutor")
    public void generate(Authentication authentication, String projectId, String arId,
                         String businessKey,
                         Map<String, Object> formSubmissionsSubjectToFilesGeneration,
                         GenerateFilesParticipantType participantType, DelegateExecution delegateExecution) {

        try {
            if (participantType.equals(GenerateFilesParticipantType.CITIZEN)) {
                String formDataSubmissionKey = arId.concat(ProcessConstants.FORMA_REQUEST_SUFFIX);
                List<EDeliveryFilesPackageDetails> eDeliveryFilesPackages = new ArrayList<>();
                for (Map.Entry<String, Object> submission : formSubmissionsSubjectToFilesGeneration.entrySet()) {
                    if (EFormsUtils.isLocalVariableFormSubmission(submission.getKey())) {
                        JacksonJsonNode nestedFormSubmissionJson = (JacksonJsonNode) Spin.JSON(
                                submission.getValue());
                        DocumentContext nestedSubmissionContext = JsonPath.using(jsonPathConfiguration)
                                .parse(nestedFormSubmissionJson.toString());
                        boolean filesPackageIsSignable = nestedSubmissionContext.read(
                                processConfProperties.getFormIsSignableJsonPathQuery());
                        String nestedFormApiPath = EFormsUtils.getFormApiPathFromSubmissionKey(submission.getKey());
                        HashMap<String, Object> nestedFormDef = formsService.getForm(
                                new ResourcePath(projectId, nestedFormApiPath), authentication);
                        String filename = nestedSubmissionContext.read(processConfProperties.getFormFileCodeJsonPathQuery());
                        EDeliveryFilesPackageDetails filesPackage = createFormFilesPackage(authentication, nestedFormDef,
                                nestedFormSubmissionJson, projectId,
                                arId, businessKey,
                                formDataSubmissionKey,
                                submission.getKey().replace(ProcessConstants.SUBMISSION_DATA, ""), filename,
                                filesPackageIsSignable, delegateExecution.getProcessInstanceId(),
                                RequestType.REQUEST
                        );
                        filesPackage.setConsolidating(true);
                        eDeliveryFilesPackages.add(filesPackage);
                    }
                }
                if (!eDeliveryFilesPackages.isEmpty()) {
                    runtimeService.setVariable(delegateExecution.getProcessInstanceId(),
                            ProcessConstants.EDELIVERY_FILES_PACKAGE + arId +
                                    ProcessConstants.FORMA_REQUEST_SUFFIX,
                            eDeliveryFilesPackages);
                    runtimeService.setVariable(delegateExecution.getProcessInstanceId(),
                            ProcessConstants.EDELIVERY_FILES_PACKAGE_STATUS,
                            EDeliveryFilesPackageStatus.FINISHED.toString());
                }
            } else {
                List<EDeliveryFilesPackageDetails> eDeliveryFilesPackages = new ArrayList<>();
                for (Map.Entry<String, Object> submission : formSubmissionsSubjectToFilesGeneration.entrySet()) {
                    if (EFormsUtils.isLocalVariableFormSubmission(submission.getKey())) {
                        JacksonJsonNode nestedFormSubmissionJson = (JacksonJsonNode) Spin.JSON(
                                submission.getValue());
                        DocumentContext nestedSubmissionContext = JsonPath.using(jsonPathConfiguration)
                                .parse(nestedFormSubmissionJson.toString());
                        String nestedFormApiPath = EFormsUtils.getFormApiPathFromSubmissionKey(submission.getKey());
                        HashMap<String, Object> nestedFormDef = formsService.getForm(
                                new ResourcePath(projectId, nestedFormApiPath), authentication);
                        String filename = nestedSubmissionContext.read(processConfProperties.getFormFileCodeJsonPathQuery());
                        EDeliveryFilesPackageDetails filesPackage = createFormFilesPackage(authentication, nestedFormDef,
                                nestedFormSubmissionJson, projectId,
                                arId, businessKey,
                                null,
                                submission.getKey().replace(ProcessConstants.SUBMISSION_DATA, ""), filename,
                                true, delegateExecution.getProcessInstanceId(), RequestType.RESPONSE
                        );
                        filesPackage.setConsolidating(false);
                        eDeliveryFilesPackages.add(filesPackage);
                    }
                }
                if (!eDeliveryFilesPackages.isEmpty()) {
                    runtimeService.setVariable(delegateExecution.getProcessInstanceId(),
                            ProcessConstants.EDELIVERY_FILES_PACKAGE + arId +
                                    ProcessConstants.FORMA_RESPONSE_SUFFIX,
                            eDeliveryFilesPackages);
                    runtimeService.setVariable(delegateExecution.getProcessInstanceId(),
                            ProcessConstants.EDELIVERY_FILES_PACKAGE_STATUS,
                            EDeliveryFilesPackageStatus.FINISHED.toString());
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            runtimeService.setVariable(delegateExecution.getProcessInstanceId(),
                    ProcessConstants.EDELIVERY_FILES_PACKAGE_STATUS,
                    EDeliveryFilesPackageStatus.FAILED.toString());
        }
    }

    public EDeliveryFilesPackageDetails createFormFilesPackage(Authentication authentication,
                                                               HashMap<String, Object> formDef,
                                                               JacksonJsonNode formSubmissionJson,
                                                               String projectId, String arId,
                                                               String businessKey, String documentId,
                                                               String formId, String filename,
                                                               boolean isSignable, String processInstanceId,
                                                               RequestType requestType) throws Exception {
        HashMap<String, Object> formSubmission = new ObjectMapper().readValue(formSubmissionJson.toString(),
                new TypeReference<>() {
                });
        SubmissionPdf formPdfRepresentation = pdfService.downloadSubmission(projectId, formDef, authentication,
                formSubmission,
                processConfProperties.getEDeliveryPdfFilename());

        Map<String, Object> caseMap = new ObjectMapper().convertValue(
                runtimeService.getVariable(processInstanceId,"case"),Map.class);
        Map<String, Object> documentJson = JsonPath.parse(formSubmissionJson.toString())
                .put("$.data", "caseDataSource", caseMap)
                .json();
        Document document = jsonStringToXml(Spin.JSON(documentJson).toString());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document templateDocument = null;
        if(requestType == RequestType.REQUEST) {
            if (isRequestMainForm(formId)) {
                templateDocument = parser.parse(serviceRequestTemplate.getInputStream());
            } else {
                templateDocument = parser.parse(serviceRequestOptionalTemplate.getInputStream());
            }
        } else {
            templateDocument = parser.parse(serviceRequestResponseTemplate.getInputStream());
        }

        processElements(templateDocument.getChildNodes(), templateDocument, document);
        removeEmptyChildElements(templateDocument.getDocumentElement());
        cleanSpaces(templateDocument.getDocumentElement());
        String formXmlRepresentation = xmlToXmlString(templateDocument);
        if (isRequestMainForm(formId)) {
            validate(formXmlRepresentation, businessKey);
        }

        DocumentContext context = JsonPath.using(jsonPathConfiguration).parse(formSubmission);
        String documentTitle = context.read("$.data.documentTitle");
        String documentCode = context.read("$.data.documentCode");

        SubmissionPdf formPdfWithMetaData = attachMetaDataToPdf(formSubmissionJson.toString(), formXmlRepresentation,
                formPdfRepresentation);
        List<MinioFile> files = createAndSaveMinioFilesList(arId, businessKey, documentId, projectId,
                formId, filename, formPdfWithMetaData);

        EDeliveryFilesPackageDetails eDeliveryFilesPackageDetails = new EDeliveryFilesPackageDetails(isSignable, files);
        eDeliveryFilesPackageDetails.setFileTitle(documentTitle);
        eDeliveryFilesPackageDetails.setFileCode(documentCode);
        eDeliveryFilesPackageDetails.setBusinessKey(businessKey);
        eDeliveryFilesPackageDetails.setFormAlias(EFormsUtils.getFormApiPathFromSubmissionKey(formId));
        return eDeliveryFilesPackageDetails;
    }

    private List<MinioFile> createAndSaveMinioFilesList(final String arId, final String businessKey,
                                                        final String documentId,
                                                        final String projectId,
                                                        final String formId, final String filename,
                                                        final SubmissionPdf pdfWithMetaData)
            throws Exception {

        return List.of(
                this.minioService.saveFile(String.join("-", arId, businessKey, filename)
                                + ".".concat(CommonUtils.PDF),
                        pdfWithMetaData.getContent(), pdfWithMetaData.getContentType(), projectId, businessKey,
                        documentId,
                        formId));

    }

    @SneakyThrows
    public SubmissionPdf attachMetaDataToPdf(final String formSubmissionJson, final String xml,
                                             final SubmissionPdf formPdf) {
        final PDDocument document = PDDocument.load(formPdf.getContent());

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final PDDocumentInformation info = document.getDocumentInformation();
        info.setCustomMetadataValue(formPdf.getFileName() + "_".concat(CommonUtils.XML), xml);
        info.setCustomMetadataValue(formPdf.getFileName() + "_".concat(CommonUtils.JSON), formSubmissionJson);

        document.setDocumentInformation(info);
        document.save(byteArrayOutputStream);
        document.close();

        formPdf.setContent(byteArrayOutputStream.toByteArray());
        return formPdf;

    }

    private boolean isRequestMainForm(String formId) {
        return formId.endsWith(ProcessConstants.FORMA_REQUEST_SUFFIX) ||
                formId.endsWith(ProcessConstants.FORMA_REQUEST_EMBEDDED_SUFFIX);
    }

    private void processElements(NodeList nodeList, Document templateDocument, Document dataDocument) throws Exception {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                if (childNode.getFirstChild() != null) {
                    Node objectNode = null;
                    String nodeValue = childNode.getFirstChild().getNodeValue();
                    Matcher matcher = Pattern.compile("(#\\{[^}]+})").matcher(nodeValue);
                    while (matcher.find()) {
                        String fullPath = StringUtils.substringBetween(matcher.group(1), "#{", "}");
                        String path = StringUtils.substringBefore(fullPath, "??").trim();
                        String defaultValue = StringUtils.substringAfter(fullPath, "??").trim();
                        XPathFactory xPathfactory = XPathFactory.newInstance();
                        XPath xpath = xPathfactory.newXPath();
                        XPathExpression exprResult = xpath.compile(path);
                        NodeList nl = (NodeList) exprResult.evaluate(dataDocument, XPathConstants.NODESET);
                        if (nl.getLength() > 0) {
                            if (Arrays.stream(specificContentTags.split("")).anyMatch(s -> s.equals(childNode.getNodeName()))) {
                                objectNode = nl.item(0);
                                break;
                            } else {
                               try {
                                   nodeValue = StringUtils.replaceOnce(nodeValue, matcher.group(1), nl.item(0).getFirstChild().getNodeValue());
                               } catch (Exception e) {
                                   nodeValue = StringUtils.replaceOnce(nodeValue, matcher.group(1), defaultValue);
                               }

                            }
                        } else {
                            nodeValue = StringUtils.replaceOnce(nodeValue, matcher.group(1), defaultValue);
                        }
                    }
                    if (objectNode != null) {
                        Node contentNode = templateDocument.importNode(objectNode, true);
                        childNode.removeChild(childNode.getFirstChild());
                        childNode.appendChild(contentNode);
                        continue;
                    } else {
                        for (int a = 0; a < childNode.getAttributes().getLength(); a++) {
                            if ("type".equalsIgnoreCase(childNode.getAttributes().item(a).getNodeName()) &&
                                    "date".equalsIgnoreCase(childNode.getAttributes().item(a).getNodeValue()) &&
                                    nodeValue.length() > 10) {
                                nodeValue = nodeValue.substring(0, 10);
                            }
                            childNode.getAttributes().removeNamedItem(childNode.getAttributes().item(a).getNodeName());
                        }
                        childNode.getFirstChild().setNodeValue(nodeValue);
                    }
                }
                NodeList children = childNode.getChildNodes();
                if (children != null) {
                    processElements(children, templateDocument, dataDocument);
                }
            }
        }
    }

    private static void removeEmptyChildElements(Node parentElement) {
        List<Node> toRemove = new LinkedList<>();
        NodeList children = parentElement.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeEmptyChildElements(child);
                if (elementIsRedundant(child)) {
                    toRemove.add(child);
                    if (child.getPreviousSibling() != null) {
                        toRemove.add(child.getPreviousSibling());
                    }
                }
            }
        }
        for (Node childElement : toRemove) {
            parentElement.removeChild(childElement);
        }
    }

    private static boolean elementIsRedundant(Node element) {
        if (element.hasAttributes()) {
            return false;
        }
        if (!element.hasChildNodes()) {
            return true;
        }
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return false;
            }
            if (child.getNodeType() == Node.TEXT_NODE) {
                String value = child.getNodeValue();
                if (value != null && !value.matches("\\s*")) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void cleanSpaces(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int n = childNodes.getLength() - 1; n >= 0; n--) {
            Node child = childNodes.item(n);
            short nodeType = child.getNodeType();

            if (nodeType == Node.ELEMENT_NODE) {
                cleanSpaces(child);
            } else if (nodeType == Node.TEXT_NODE) {
                String trimmedNodeVal = child.getNodeValue().trim();
                if (trimmedNodeVal.length() == 0)
                    node.removeChild(child);
                else
                    child.setNodeValue(trimmedNodeVal);
            } else if (nodeType == Node.COMMENT_NODE) {
                node.removeChild(child);
            }
        }
    }

    private void validate(String xml, String businessKey) throws Exception {
        try {
            Source xmlFile = new StreamSource(new StringReader(xml));
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new StreamSource(validationSchema.getInputStream()));
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
        } catch (SAXException e) {
            log.warn(String.format("XML file for %s is NOT valid. Reason: %s", businessKey, e.getMessage()));
            log.warn(String.format("XML for %s:\n %s", businessKey, xml));
        }
    }

    private String jsonToJsonString(JsonNode json) throws Exception {
        if (json == null) return null;
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return jsonMapper.writeValueAsString(json);
    }

    private JsonNode jsonStringToJson(String json) throws Exception {
        if (json == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }

    private Document jsonStringToXml(String json) throws Exception {
        if (json == null) return null;
        JsonNode node = jsonStringToJson(json);
        return jsonToXml(node);
    }

    private Document jsonToXml(JsonNode json) throws Exception {
        if (json == null) return null;

        JsonNode data = json.get("data");
        if (data == null) return null;

        ObjectMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String xml = "";
        if (data instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) json;
            if (objectNode.size() > 0) {
                Map.Entry<String, JsonNode> entry = json.fields().next();
                xml = xmlMapper.writer()
                        .with(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
                        .withRootName(entry.getKey())
                        .writeValueAsString(entry.getValue());
            }
        } else {
            xml = xmlMapper.writer()
                    .with(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
                    .writeValueAsString(json);
        }
        return xmlStringToXml(xml);
    }

    private String xmlToXmlString(Document doc) throws Exception {
        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(new DOMSource(doc), new StreamResult(sw));
        return sw.toString();
    }

    private Document xmlStringToXml(String xml) throws Exception {
        if (xml == null) return null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private JsonNode xmlStringToJson(String xml) throws Exception {
        if (xml == null) return null;
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readTree(xml.getBytes());
    }

    private String xmlStringToJsonString(String xml) throws Exception {
        if (xml == null) return null;
        JsonNode node = xmlStringToJson(xml);
        return jsonToJsonString(node);
    }

    private enum RequestType {
        REQUEST,
        RESPONSE
    }

}


