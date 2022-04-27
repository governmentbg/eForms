package com.bulpros.eforms.processengine.camunda.model;

import com.bulpros.eforms.processengine.minio.model.MinioFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EDeliveryFilesPackage {

    private boolean isSignable;
    private List<MinioFile> files;
}
