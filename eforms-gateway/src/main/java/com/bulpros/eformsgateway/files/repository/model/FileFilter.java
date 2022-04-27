package com.bulpros.eformsgateway.files.repository.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@RequiredArgsConstructor
public class FileFilter {

    @NotEmpty
    private String businessKey;
    private String documentId;
    @NotEmpty
    private String formId;
    @NotEmpty
    private String filename;
}
