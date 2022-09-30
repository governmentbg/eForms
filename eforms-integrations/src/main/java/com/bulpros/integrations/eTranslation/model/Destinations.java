package com.bulpros.integrations.eTranslation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Destinations {
    private List<String> emailDestinations;
    private List<String> httpDestinations;
}
