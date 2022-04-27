package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.EDeliveryFilesPackage;
import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.minio.model.MinioFile;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.service.FormsService;
import com.bulpros.formio.service.PdfService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.spin.Spin;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class GeneratePdfListener implements TaskListener {

    private final FormService formService;
    private final FormsService formsService;
    private final PdfService pdfService;
    private final MinioService minioService;
    private final CamundaProcessRepository processService;
    private final ConfigurationProperties processConfProperties;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public void notify(DelegateTask delegateTask) {
        Authentication authentication = this.authenticationFacade.getAuthentication();
        try {
            TaskFormData taskFormData = this.formService.getTaskFormData(delegateTask.getId());
            String formKey = taskFormData.getFormKey();
            String formDataSubmissionKey = EFormsUtils.getFormDataSubmissionKey(formKey);
            String formApiPath = EFormsUtils.getFormApiPath(formKey);
            String businessKey = this.processService.getBusinessKey(delegateTask.getProcessInstanceId());
            ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
            String projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                    .getValue(delegateTask.getExecution());
            String arId = (String) expressionManager.createExpression(processConfProperties.getArIdPathExpr())
                    .getValue(delegateTask.getExecution());

            var localVariables = delegateTask.getVariablesLocal();
            var embeddedFormLocalVariable = localVariables.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().endsWith(ProcessConstants.EMBEDDED_SUFFIX))
                    .collect(Collectors.toMap(e->e.getKey(), e->e.getValue()));
            if (embeddedFormLocalVariable.isEmpty()) {
                JacksonJsonNode submissionJson = (JacksonJsonNode) Spin.JSON(delegateTask
                        .getVariable(ProcessConstants.SUBMISSION_DATA + formDataSubmissionKey));
                HashMap<String, Object> formDef = formsService.getForm(new ResourcePath(projectId, formApiPath), authentication);
                boolean generateFilesPackage = JsonPath.read(submissionJson.toString(),
                        processConfProperties.getFormGenerateFilesPackageJsonPathQuery());
                boolean filesPackageIsSignable = JsonPath.read(submissionJson.toString(),
                        processConfProperties.getFormIsSignableJsonPathQuery());
                if (generateFilesPackage) {
                    String filename = JsonPath.read(submissionJson.toString(), processConfProperties.getFormFilenameJsonPathQuery());
                    EDeliveryFilesPackage eDeliveryFilesPackage = createFormFilesPackage(authentication, formDef, submissionJson, projectId,
                            arId, businessKey, null, formDataSubmissionKey, filename, filesPackageIsSignable);
                    delegateTask.setVariable(ProcessConstants.EDELIVERY_FILES_PACKAGE + formDataSubmissionKey, Spin.JSON(eDeliveryFilesPackage));
                    delegateTask.setVariable(ProcessConstants.PROCESS_HAS_SIGNING, filesPackageIsSignable);
                }
            } else {
                List<EDeliveryFilesPackage> eDeliveryFilesPackages = new ArrayList<>();
                boolean processHasSigning = false;
                for (Map.Entry<String, Object> localVariable : delegateTask.getVariablesLocal().entrySet()) {
                    if (EFormsUtils.isLocalVariableFormSubmission(localVariable.getKey())) {
                        JacksonJsonNode nestedFormSubmissionJson = (JacksonJsonNode) Spin.JSON(
                                delegateTask.getVariable(localVariable.getKey()));
                        boolean generateFilesPackage = JsonPath.read(nestedFormSubmissionJson.toString(),
                                processConfProperties.getFormGenerateFilesPackageJsonPathQuery());
                        boolean filesPackageIsSignable = JsonPath.read(nestedFormSubmissionJson.toString(),
                                processConfProperties.getFormIsSignableJsonPathQuery());
                        if (generateFilesPackage) {
                            String nestedFormApiPath = EFormsUtils.getFormApiPathFromSubmissionKey(localVariable.getKey());
                            HashMap<String, Object> nestedFormDef = formsService.getForm(new ResourcePath(projectId, nestedFormApiPath), authentication);
                            String filename = JsonPath.read(nestedFormSubmissionJson.toString(), processConfProperties.getFormFilenameJsonPathQuery());
                            eDeliveryFilesPackages.add(createFormFilesPackage(authentication, nestedFormDef, nestedFormSubmissionJson, projectId,
                                    arId, businessKey,
                                    formDataSubmissionKey,
                                    localVariable.getKey().replace(ProcessConstants.SUBMISSION_DATA, ""), filename,
                                    filesPackageIsSignable
                            ));
                        }
                        processHasSigning = filesPackageIsSignable;
                    }
                }
                delegateTask.setVariable(ProcessConstants.PROCESS_HAS_SIGNING, processHasSigning);
                if (!eDeliveryFilesPackages.isEmpty()) {
                    delegateTask.setVariable(ProcessConstants.EDELIVERY_FILES_PACKAGE + formDataSubmissionKey,
                            Spin.JSON(eDeliveryFilesPackages));
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(exception.getMessage());
        }
    }

    private EDeliveryFilesPackage createFormFilesPackage(Authentication authentication, HashMap<String, Object> formDef,
                                                         JacksonJsonNode formSubmissionJson, String projectId, String arId,
                                                         String businessKey, String documentId, String formId, String filename,
                                                         boolean isSignable) throws Exception {
        List<MinioFile> files = new ArrayList<>();
        HashMap<String, Object> formSubmission = new ObjectMapper().readValue(formSubmissionJson.toString(), new TypeReference<>() {
        });
        SubmissionPdf formPdf = pdfService.downloadSubmission(projectId, formDef, authentication, formSubmission,
                processConfProperties.getEDeliveryPdfFilename());

        files.add(this.minioService.saveFile(String.join("-", arId, businessKey, filename) + ".pdf",
                formPdf.getContent(), formPdf.getContentType(), projectId, businessKey, documentId, formId));

        files.add(this.minioService.saveFile(String.join("-", arId, businessKey, filename) + ".json",
                formSubmissionJson.toString().getBytes(), Variables.SerializationDataFormats.JSON.getName(), projectId, businessKey,
                documentId, formId));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(formSubmissionJson.toString());
        ObjectMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String xml = xmlMapper.writer()
                .with(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
                .writeValueAsString(json)
                .replace("ObjectNode", "document");
        files.add(this.minioService.saveFile(String.join("-", arId, businessKey, filename) + ".xml",
                xml.getBytes(), Variables.SerializationDataFormats.XML.getName(), projectId, businessKey,
                documentId, formId));
        return new EDeliveryFilesPackage(isSignable, files);
    }
}
