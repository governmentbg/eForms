package com.bulpros.integrations.regix.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RegixSearchDataParameter {

    private RegixSearchDataParameterTypeEnum parameterType;
    private String parameterStringValue;
    private Date parameterDateValue;
    private BigDecimal parameterNumberValue;
    private Integer precision;
    private List<RegixSearchDataParameterMap> parameters = new ArrayList<>();
    private List<RegixSearchDataAttributeMap> attributes = new ArrayList<>();

}
