package com.bulpros.formio.repository.formio;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.PdfRepository;
import com.bulpros.formio.repository.client.FormioHttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class PdfRepositoryImpl extends BaseRepository implements PdfRepository {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public SubmissionPdf downloadSubmission(String projectId, User user, HashMap<String, Object> form, HashMap<String, Object> submission, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        JsonNode formNode = objectMapper.convertValue(form, JsonNode.class);
        JsonNode submissionNode = objectMapper.convertValue(submission, JsonNode.class);
        ObjectNode body = objectMapper.createObjectNode();
        body.set("form", formNode);
        body.set("submission", submissionNode);
        HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

        var result = this.formioHttpClient = new FormioHttpClient.Builder(restTemplate, getCreatePdfWithSubmissionUrl(projectId))
                .method(HttpMethod.POST)
                .entity(request)
                .returnType(new ParameterizedTypeReference<byte[]>(){})
                .build();
        ResponseEntity<Object> arrBytes = result.getObject(user);

        SubmissionPdf submissionPdf = new SubmissionPdf();
        submissionPdf.setContentType("application/pdf");
        submissionPdf.setContent((byte[]) arrBytes.getBody());
        submissionPdf.setFileName( filename);

        return submissionPdf;
    }
}
