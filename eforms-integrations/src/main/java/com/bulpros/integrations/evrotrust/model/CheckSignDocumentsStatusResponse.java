package com.bulpros.integrations.evrotrust.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckSignDocumentsStatusResponse {

    private Integer status; // 1 - Pending, 2 - Signed, 3 - Rejected, 4 - Expired, 5 - Failed,
                            // 6 - Withdrawn, 7 - Undeliverable, 99 - On hold
    private Boolean isProcessing;

}
