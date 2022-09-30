package com.bulpros.eforms.processengine.camunda.model.enums;

import com.bulpros.eforms.processengine.camunda.model.EDeliveryFilesPackage;
import com.bulpros.eforms.processengine.minio.model.MinioFile;

import java.util.Collections;
import java.util.List;

public enum EDeliveryFilesClassification {

    ORIGINAL {
        @Override
        public List<MinioFile> processableFiles(EDeliveryFilesPackage eDeliveryFilesPackage) {
            return eDeliveryFilesPackage.isSignable() ? Collections.emptyList() : eDeliveryFilesPackage.getFiles();
        }
    },
    SIGNED {
        @Override
        public List<MinioFile> processableFiles(EDeliveryFilesPackage eDeliveryFilesPackage) {
            return eDeliveryFilesPackage.isSignable() ? eDeliveryFilesPackage.getFiles() : Collections.emptyList();
        }
    },
    ATTACHMENT {
        @Override
        public List<MinioFile> processableFiles(EDeliveryFilesPackage eDeliveryFilesPackage) {
            return eDeliveryFilesPackage.isSignable() ? Collections.emptyList() : eDeliveryFilesPackage.getFiles();
        }
    };

    public abstract List<MinioFile> processableFiles(EDeliveryFilesPackage eDeliveryFilesPackage);
}
