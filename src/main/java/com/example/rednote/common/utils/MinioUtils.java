package com.example.rednote.common.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.rednote.config.MinioProperties;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MinioUtils {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public void uploadFile(MultipartFile file, String objectName, String path) throws Exception {
        String fullObjectName = path.endsWith("/") ? path + objectName : path + "/" + objectName;
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(fullObjectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
    }

    public void uploadFile(MultipartFile file, String objectName) throws Exception {
        uploadFile(file, objectName, "");
    }

    public void uploadFile(MultipartFile file) throws Exception {
        uploadFile(file, file.getOriginalFilename());
    }

    public String getPublicUrl(String objectName) {
        String encodedObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8)
                .replace("+", "%20");
        String publicUrl = minioProperties.getEndpoint() + "/"
                + minioProperties.getBucketName() + "/"
                + encodedObjectName;
        return publicUrl;
    }

}
