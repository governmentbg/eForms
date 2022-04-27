package com.bulpros.eformsgateway.files.repository;

import io.minio.*;
import io.minio.messages.Item;

public interface MinioRepository {
    ObjectWriteResponse putObject(byte[] content, String contentType, String projectId, String location) throws Exception;

    GetObjectResponse getObject(String projectId, String location);

    void removeObject(String projectId, String location);

    StatObjectResponse statObject(String projectId, String location);

    Iterable<Result<Item>> getObjectsForPrefix(String projectId, String prefix);
}
