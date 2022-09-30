package com.bulpros.eforms.processengine.camunda.model;

import com.bulpros.eforms.processengine.minio.model.MinioFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EDeliveryFilesPackageDetails extends EDeliveryFilesPackage {
    private boolean isConsolidating;
    private String fileTitle;
    private String fileCode;
    private String businessKey;
    private String formAlias;

    public EDeliveryFilesPackageDetails(boolean isSignable, List<MinioFile> files) {
        super(isSignable, files);
    }
}
