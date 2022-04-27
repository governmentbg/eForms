package com.bulpros.eforms.processengine.minio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MinioFile {

    private String filename;
    private byte[] content;
    private String contentType;
    private String location;
}
