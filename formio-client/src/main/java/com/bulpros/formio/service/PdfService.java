package com.bulpros.formio.service;


import com.bulpros.formio.model.SubmissionPdf;
import org.springframework.security.core.Authentication;

import java.util.HashMap;

public interface PdfService {

    SubmissionPdf downloadSubmission(String projectId, HashMap<String, Object> form, Authentication authentication, HashMap<String, Object> submission, String filename);

}
