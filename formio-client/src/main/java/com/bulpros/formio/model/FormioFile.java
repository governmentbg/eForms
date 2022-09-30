package com.bulpros.formio.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormioFile {

    private String storage;
    private String bucket;
    private String key;
    private String acl;
    private String name;
    private String url;
    private String size;
    private String type;
    private String originalName;
}
