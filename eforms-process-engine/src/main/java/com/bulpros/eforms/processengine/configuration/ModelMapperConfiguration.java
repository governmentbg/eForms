package com.bulpros.eforms.processengine.configuration;

import com.bulpros.eforms.processengine.egov.model.eservice.AssuranceLevelEnum;
import com.bulpros.eforms.processengine.egov.model.eservice.EServiceDetailsInfo;
import com.bulpros.eforms.processengine.egov.model.eservice.EServiceDetailsSubmission;
import com.bulpros.eforms.processengine.egov.model.eservice.YesNoEnum;
import org.camunda.bpm.model.bpmn.impl.instance.UserTaskImpl;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {
    private Object HashMap;

    Converter<String, String> getAssuranceLevelByName = new AbstractConverter<String, String>() {
        @Override
        protected String convert(String source) {
            if (source == null || source.isEmpty()) {
                return AssuranceLevelEnum.NONE.name();
            }
            try {
                return AssuranceLevelEnum.getEnumByType(source).name();
            }
            catch (Exception е){
                return AssuranceLevelEnum.NONE.name();
            }
        }
    };

    Converter<String, Boolean> getIsInternalService = new AbstractConverter<String, Boolean>() {
        @Override
        protected Boolean convert(String source) {
            if (source == null || source.isEmpty()) {
                return YesNoEnum.NO.isValue();
            }
            return YesNoEnum.getEnumByType(source).isValue();
        }
    };

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(UserTaskImpl.class, com.bulpros.eforms.processengine.camunda.model.UserTask.class)
            .addMappings(mapper -> {
                mapper.map(UserTaskImpl::getCamundaAssignee, com.bulpros.eforms.processengine.camunda.model.UserTask::setAssignee);
                mapper.map(UserTaskImpl::getCamundaDueDate, com.bulpros.eforms.processengine.camunda.model.UserTask::setDueDate);
                mapper.map(UserTaskImpl::getCamundaFormKey, com.bulpros.eforms.processengine.camunda.model.UserTask::setFormKey);
        });


        modelMapper.createTypeMap(EServiceDetailsInfo.class, EServiceDetailsSubmission.class)
                .addMappings(mapper -> {
                    mapper.map(EServiceDetailsInfo::getServiceNumber, EServiceDetailsSubmission::setArId);
                    mapper.map(EServiceDetailsInfo::getServiceName, EServiceDetailsSubmission::setServiceName);
                    mapper.using(getAssuranceLevelByName).map(EServiceDetailsInfo::getSecurityLevel, EServiceDetailsSubmission::setRequiredSecurityLevel);
                    mapper.using(getIsInternalService).map(EServiceDetailsInfo::getIsInternalAdminService, EServiceDetailsSubmission::setIsInternalAdminService);
                });

        return modelMapper;
    }
}
