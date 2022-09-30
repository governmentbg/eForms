package com.bulpros.eformsgateway.form.web.controller.dto;

import com.bulpros.formio.dto.ResourceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CaseDetailsDto {
    List<UserProfileDto> signeesProfiles;
    ResourceDto applicant;
    String requiredSignaturesSelect;
}
