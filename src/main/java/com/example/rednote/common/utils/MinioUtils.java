package com.example.rednote.common.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.rednote.config.MinioProperties;
import com.example.rednote.model.exception.MinioUtilsException;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public String getObjectName(String url) {
        if (StrUtil.isBlank(url)) {
            throw new MinioUtilsException("文件 URL 不能为空");
        }

        String prefix = getPublicUrl("");
        if (!url.startsWith(prefix)) {
            throw new MinioUtilsException("文件 URL 前缀错误");
        }

        try {
            String encodedPart = url.substring(prefix.length());
            return URLDecoder.decode(encodedPart, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("MinIO 路径解析异常, url: {}", url, e);
            throw new MinioUtilsException("文件 URL 路径解析失败");
        }
    }

}
