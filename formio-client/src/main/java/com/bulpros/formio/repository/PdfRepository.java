package com.bulpros.formio.repository;

import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.model.User;

import java.util.HashMap;

public interface PdfRepository {

    SubmissionPdf downloadSubmission(String projectId, User user, HashMap<String, Object> form, HashMap<String, Object> submission, String filename);
}
