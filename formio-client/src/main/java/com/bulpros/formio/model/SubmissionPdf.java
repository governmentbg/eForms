package com.bulpros.formio.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionPdf {

    private String fileName;
    private byte[] content;
    private String contentType;

}
