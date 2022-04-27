package com.bulpros.eforms.processengine.edelivery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationMailRequest {
    private String to;
    private String subject;
    private String body;
}
