package com.bulpros.eformsgateway.files.repository;

import com.bulpros.eformsgateway.eformsintegrations.exception.ServiceNotAvailableException;
import com.bulpros.eformsgateway.eformsintegrations.exception.SeverityEnum;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioRepositoryImpl implements MinioRepository {

    private final MinioClient minioClient;

    @Override
    @Retryable(value = {ErrorResponseException.class, ServerException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public ObjectWriteResponse putObject(byte[] content, String contentType, String projectId, String location) throws Exception {
        try {
            return minioClient.putObject(PutObjectArgs.builder().bucket(projectId).object(location).
                    contentType(contentType).stream(new ByteArrayInputStream(content), content.length, 0).build());
        }catch (ErrorResponseException | ServerException e) {
            throw e;
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            log.error(e.getMessage());
            throw new Exception(e.getMessage(), e);
        }
    }

    @Override
    public GetObjectResponse getObject(String projectId, String location) {
        try {
            return minioClient
                    .getObject(GetObjectArgs.builder().
                            bucket(projectId).object(location).build());
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            throw new ServiceNotAvailableException(SeverityEnum.ERROR, "MINIO.COMMUNICATION", e.getMessage());
        }
    }

    @Override
    public void removeObject(String projectId, String location) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(projectId).object(location).build());
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            throw new ServiceNotAvailableException(SeverityEnum.ERROR, "MINIO.COMMUNICATION", e.getMessage());
        }
    }

    @Override
    public StatObjectResponse statObject(String projectId, String location) {
        try {
            return minioClient.statObject(StatObjectArgs.builder()
                    .bucket(projectId).object(location).build());
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            throw new ServiceNotAvailableException(SeverityEnum.ERROR, "MINIO.COMMUNICATION", e.getMessage());
        }
    }

    @Override
    public Iterable<Result<Item>> getObjectsForPrefix(String projectId, String prefix) {
        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(projectId).prefix(prefix).recursive(true).build());
    }
}
