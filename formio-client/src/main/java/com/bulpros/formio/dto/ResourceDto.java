package com.bulpros.formio.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String _id;
    private Map<String, Object> data;
    private String modified;

}