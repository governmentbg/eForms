package com.bulpros.integrations.regix.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegixSearchDataArgument {

    private String type;
    private String xmlns;
    private List<RegixSearchDataParameterMap> parameters = new ArrayList<>();

}
