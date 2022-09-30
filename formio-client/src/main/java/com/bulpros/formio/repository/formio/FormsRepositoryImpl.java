package com.bulpros.formio.repository.formio;

import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.FormsRepository;
import com.bulpros.formio.repository.client.FormioHttpClient;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class FormsRepositoryImpl extends BaseRepository implements FormsRepository {


    @Override
    public HashMap<String, Object> getForm(ResourcePath resourcePath, User user) {
        ParameterizedTypeReference<HashMap<String, Object>> responseType =
                new ParameterizedTypeReference<>() {
                };

        var result = this.formioHttpClient = new FormioHttpClient.Builder(restTemplate,
                getFormUrl(resourcePath.getProject(), resourcePath.getProjectType(), resourcePath.getForm(), resourcePath.getFormType()))
                .method(HttpMethod.GET)
                .returnType(responseType)
                .build();
        return (HashMap<String, Object>) result.getObject(user).getBody();
    }
    
    @Override
    public JsonNode getResourceMetadata(String projectId, String resourcePath, User user) {
        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate,
                        getResourceUrl(projectId, resourcePath))
                .method(HttpMethod.GET)
                .returnType(new ParameterizedTypeReference<JsonNode>(){})
                .build();
        return formioHttpClient.getJsonResult(user).getBody();
    }
}
