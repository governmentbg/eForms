package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.formio.dto.ResourceDto;
import org.jvnet.hk2.annotations.Service;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ServiceSupplierService{

    List<ResourceDto> getActiveSuppliers (String projectId, Authentication authentication);
}