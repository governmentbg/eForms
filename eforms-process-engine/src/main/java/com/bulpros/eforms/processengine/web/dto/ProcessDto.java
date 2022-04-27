package com.bulpros.eforms.processengine.web.dto;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessDto {

    private String processInstanceId;
    private String businessKey;
    private ArrayList<LaneDto> lanes = new ArrayList<>();

}