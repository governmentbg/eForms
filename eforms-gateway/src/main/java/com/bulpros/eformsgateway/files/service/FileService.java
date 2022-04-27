package com.bulpros.eformsgateway.files.service;

import com.bulpros.eformsgateway.files.repository.model.FileDto;
import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import org.springframework.http.codec.multipart.FilePart;

import java.util.List;

public interface FileService {

    FileDto store(String filename, FilePart filePart, String contentType, String projectId, FileFilter fileFilter);

    FileDto load(String projectId, FileFilter fileFilter);

    FileDto load(String projectId, String filePath);

    void delete(String projectId, FileFilter fileFilter);

    List<FileDto> caseFiles(String projectId, FileFilter fileFilter);
}
