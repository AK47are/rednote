package com.example.rednote.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.rednote.common.utils.MinioUtils;
import com.example.rednote.service.FileService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioUtils minioUtils;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        minioUtils.uploadFile(file, objectName);
        return minioUtils.getPublicUrl(objectName);
    }
}
