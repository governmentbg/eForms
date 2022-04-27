package com.bulpros.eformsgateway.files.service;

import com.bulpros.eformsgateway.eformsintegrations.exception.ServiceNotAvailableException;
import com.bulpros.eformsgateway.files.repository.MinioRepository;
import com.bulpros.eformsgateway.files.repository.model.FileDto;
import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.process.repository.utils.EFormsUtils;
import io.minio.GetObjectResponse;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioRepository minioRepository;

    @Override
    public FileDto store(String filename, FilePart filePart, String contentType, String projectId, FileFilter fileFilter) {
        String location = createLocation(fileFilter);
        Mono<byte[]> content = readFilePartContent(filePart);
        try {
            minioRepository.putObject(content.block(), contentType, projectId, location);
            return FileDto.builder().name(filename)
                    .contentType(contentType).build();
        } catch (Exception e) {
            throw new ServiceNotAvailableException(e.getMessage());
        }
    }

    @Override
    public FileDto load(String projectId, FileFilter fileFilter) {
        String location = createLocation(fileFilter);
        GetObjectResponse response = minioRepository.getObject(projectId, location);
        String contentTypeHeader = response.headers().get("Content-Type");
        return FileDto.builder().name(response.object())
                .contentType(contentTypeHeader)
                .resource(new InputStreamResource((InputStream) response))
                .build();
    }

    @Override
    public FileDto load(String projectId, String filePath) {
        String location = createDownloadsLocation(filePath);
        GetObjectResponse response = minioRepository.getObject(projectId, location);
        String contentTypeHeader = response.headers().get("Content-Type");
        return FileDto.builder().name(response.object())
                .contentType(contentTypeHeader)
                .resource(new InputStreamResource((InputStream) response))
                .build();
    }


    @Override
    public void delete(String projectId, FileFilter fileFilter) {
        String location = createLocation(fileFilter);
        minioRepository.removeObject(projectId, location);
    }

    @Override
    public List<FileDto> caseFiles(String projectId, FileFilter fileFilter) {
        List<FileDto> files = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioRepository.getObjectsForPrefix(projectId, fileFilter.getBusinessKey());
            for (Result<Item> result : results) {
                StatObjectResponse statObject = minioRepository.statObject(projectId, result.get().objectName());
                FileDto fileDto = FileDto.builder().bucket(projectId)
                        .storage("s3")
                        .name(StringUtils.substringAfterLast(statObject.object(), "/"))
                        .originalName(StringUtils.substringAfterLast(statObject.object(), "/"))
                        .key(statObject.object())
                        .url(statObject.object())
                        .acl("public-read")
                        .size(statObject.size())
                        .contentType(statObject.contentType())
                        .lastModified(statObject.lastModified().toString())
                        .build();
                if (files.stream().noneMatch(f -> f.getName().equals(fileDto.getName()))) {
                    files.add(fileDto);
                }
                else if(fileDto.getKey().contains("common_component_attachment_sign")){
                    files = files.stream().filter(file -> !file.getName().equals(fileDto.getName())).collect(Collectors.toList());
                    files.add(fileDto);
                }
            }

        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException
                | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException
                | InternalException | IOException e) {
            log.error(e.getMessage());
            throw new ServiceNotAvailableException(e.getMessage());
        }
        return files;
    }

    Mono<byte[]> readFilePartContent(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                });
    }

    private String createLocation(FileFilter fileFilter) {
        if (StringUtils.isEmpty(fileFilter.getDocumentId())) {
            String formDataSubmissionKey = EFormsUtils.getFormDataSubmissionKey(fileFilter.getFormId());
            return String.format("%s/%s/%s", fileFilter.getBusinessKey(), formDataSubmissionKey, fileFilter.getFilename());
        } else {
            String documentDataSubmissionKey = EFormsUtils.getFormDataSubmissionKey(fileFilter.getDocumentId());
            String formDataSubmissionKey = EFormsUtils.getFormDataSubmissionKey(fileFilter.getFormId());
            return String.format("%s/%s/%s/%s", fileFilter.getBusinessKey(), documentDataSubmissionKey,
                    formDataSubmissionKey, fileFilter.getFilename());
        }
    }

    private String createDownloadsLocation(String filePath) {
        return String.format("%s/%s", "downloads", filePath);
    }
}
