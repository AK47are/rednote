package com.example.rednote.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.rednote.config.MinioProperties;
import com.example.rednote.service.FileService;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        return getPublicUrl(objectName);
    }

    private String getPublicUrl(String objectName) {
        String encodedObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8)
                .replace("+", "%20");
        String publicUrl = minioProperties.getEndpoint() + "/"
                + minioProperties.getBucketName() + "/"
                + encodedObjectName;
        return publicUrl;
    }
}
