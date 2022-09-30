package com.bulpros.formio.service.formio;

import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.PdfRepository;
import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.security.FormioUserService;
import com.bulpros.formio.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private PdfRepository pdfRepository;
    @Autowired
    private FormioUserService userService;

    @Override
    public SubmissionPdf downloadSubmission(String projectId, HashMap<String, Object> form, Authentication authentication, HashMap<String, Object> submission, String filename) {
        User user = this.userService.getUser(authentication);
       return pdfRepository.downloadSubmission(projectId, user, form, submission, filename);
    }
}
