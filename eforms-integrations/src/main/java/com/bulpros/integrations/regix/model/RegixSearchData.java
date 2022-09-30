package com.bulpros.integrations.regix.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegixSearchData {

    private String operation;
    private RegixSearchDataArgument argument;
    private RegixSearchDataContext context;

}
