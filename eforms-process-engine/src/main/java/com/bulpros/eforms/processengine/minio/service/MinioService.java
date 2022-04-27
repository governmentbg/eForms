package com.bulpros.eforms.processengine.minio.service;

import com.bulpros.eforms.processengine.minio.model.MinioFile;

public interface MinioService {
    MinioFile saveFile(String fileName, byte[] content, String contentType, String projectId, String businessKey,
                       String documentId, String formId, boolean useStaticClient) throws Exception;

    MinioFile saveFile(String fileName, byte[] content, String contentType, String projectId, String businessKey,
                       String documentId, String formId) throws Exception;

    MinioFile getFile(String projectId, MinioFile minioFile, boolean useStaticClient) throws Exception;

    MinioFile getFile(String projectId, MinioFile minioFile) throws Exception;
}
