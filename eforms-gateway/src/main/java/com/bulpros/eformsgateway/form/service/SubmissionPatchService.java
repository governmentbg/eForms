package com.bulpros.eformsgateway.form.service;

import com.bulpros.formio.dto.ResourceDto;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;


public interface SubmissionPatchService {

    String createPatchData(HashMap<String, Object> payload);

}
