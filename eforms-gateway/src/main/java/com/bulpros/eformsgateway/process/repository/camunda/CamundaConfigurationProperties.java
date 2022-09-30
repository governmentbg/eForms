package com.bulpros.eformsgateway.process.repository.camunda;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:gateway-resource-${spring.profiles.active}.properties")
public class CamundaConfigurationProperties {

    @Value("${com.bulpros.process-engine.engine.url}")
    private String processEngineUrl;

    @Value("${com.bulpros.camunda.rest.root}")
    private String processEngineRestRoot;

    @Value("${com.bulpros.camunda.process-definition}")
    private String processDefinitionResourcePath;

    @Value("${com.bulpros.camunda.process-instance}")
    private String processInstancePath;

    @Value("${com.bulpros.camunda.history}")
    private String historyPath;

    @Value("${com.bulpros.camunda.variable-instance}")
    private String variableInstancePath;

    @Value("${com.bulpros.camunda.process-definition.start-instance}")
    private String processDefintionStartInstanceResourcePath;

    @Value("${com.bulpros.camunda.task}")
    private String taskResourcePath;

    @Value("${com.bulpros.camunda.message}")
    private String messageResourcePath;

    @Value("${com.bulpros.camunda.history.task}")
    private String historyTaskResourcePath;

    @Value("${com.bulpros.process.payment.url}")
    private String paymentUrl;

    @Value("${com.bulpros.process.payment.callback}")
    private String paymentStatusCallbackResourcePath;

    @Value("${com.bulpros.process.payment.callback.remote.addresses}")
    private String paymentStatusCallbackRemoteAddresses;

    @Value("${com.bulpros.process.payment.aisClientId}")
    private String paymentStatusClientId;

    @Value("${com.bulpros.process.payment.aisSecretKey}")
    private String paymentStatusSecretKey;

    @Value("${com.bulpros.process.admin.case.message.url}")
    private String adminCaseMessageUrl;


}
