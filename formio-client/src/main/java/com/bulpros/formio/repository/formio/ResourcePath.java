package com.bulpros.formio.repository.formio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourcePath {
    private String project;
    private ValueTypeEnum projectType = ValueTypeEnum.ID; 
    private String form;
    private ValueTypeEnum formType = ValueTypeEnum.PATH;
    
    public ResourcePath(String project, String form) {
        this.project = project;
        this.form = form;
    }
}
