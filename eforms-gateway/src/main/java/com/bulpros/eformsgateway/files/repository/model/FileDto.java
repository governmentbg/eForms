package com.bulpros.eformsgateway.files.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter
@Builder
public class FileDto {

    private String storage;
    private String name;
    private String originalName;
    private String bucket;
    private String key;
    private String url;
    private String acl;
    private Long size;
    private String contentType;
    private String lastModified;
    @JsonIgnore
    private Resource resource;
}
