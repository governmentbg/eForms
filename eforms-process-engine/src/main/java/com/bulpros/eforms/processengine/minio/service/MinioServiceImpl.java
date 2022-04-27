package com.bulpros.eforms.processengine.minio.service;

import com.bulpros.eforms.processengine.minio.model.MinioFile;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient stsClient;
    private final MinioClient staticClient;

    public MinioServiceImpl(@Lazy @Qualifier("stsClient") MinioClient stsClient, @Qualifier("staticClient") MinioClient staticClient) {
        this.stsClient = stsClient;
        this.staticClient = staticClient;
    }

    @Override
    @Retryable(value = {ErrorResponseException.class, ServerException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public MinioFile saveFile(String fileName, byte[] content, String contentType, String projectId, String businessKey,
                              String documentId, String formId) throws Exception {
        return saveFile(fileName, content, contentType, projectId, businessKey, documentId, formId, true);
    }

    @Override
    @Retryable(value = {ErrorResponseException.class, ServerException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public MinioFile saveFile(String fileName, byte[] content, String contentType, String projectId, String businessKey,
                              String documentId, String formId, boolean useStaticClient) throws Exception {
        MinioClient minioClient = useStaticClient ? staticClient : stsClient;
        try {
            var projectBucket = minioClient.bucketExists(BucketExistsArgs.builder().bucket(projectId).build());
            if (!projectBucket) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(projectId).build());
            }
            String location = createLocation(businessKey, documentId, formId, fileName);
            minioClient.putObject(PutObjectArgs.builder().bucket(projectId).object(location).
                    contentType(contentType).stream(new ByteArrayInputStream(content), content.length, 0).build());
            return new MinioFile(fileName, null, contentType, location);
        } catch (ErrorResponseException | ServerException e) {
            throw e;
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            log.error(e.getMessage());
            throw new Exception("Could not save file in minio!");
        }
    }

    @Override
    @Retryable(value = {ErrorResponseException.class, ServerException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public MinioFile getFile(String projectId, MinioFile minioFile) throws Exception {
        return getFile(projectId, minioFile, true);
    }

    @Override
    @Retryable(value = {ErrorResponseException.class, ServerException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public MinioFile getFile(String projectId, MinioFile minioFile, boolean useStaticClient) throws Exception {
        MinioClient minioClient = useStaticClient ? staticClient : stsClient;
        try {
            var projectBucket = minioClient.bucketExists(BucketExistsArgs.builder().bucket(projectId).build());
            if (!projectBucket) {
                throw new MinioException();
            }
            var inputStream = minioClient
                    .getObject(GetObjectArgs.builder().bucket(projectId).object(minioFile.getLocation()).build());
            minioFile.setContent(inputStream.readAllBytes());
            return minioFile;
        } catch (ErrorResponseException | ServerException e) {
            throw e;
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            log.error(e.getMessage());
            throw new Exception("Could not get file from minio!");
        }
    }

    private String createLocation(String businessKey, String documentId, String formId, String fileName) {
        if (StringUtils.isEmpty(documentId)) {
            return String.format("%s/%s/%s", businessKey, formId, fileName);
        } else {
            return String.format("%s/%s/%s/%s", businessKey, documentId,
                    formId, fileName);
        }
    }

}
