package com.bulpros.formio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CreateSubmissionResult {
    @JsonProperty("_id")
    private String id;
    private String form;
    private String project;
    private Date created;
    private Date modified;
}
