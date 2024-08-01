package com.mgmtp.cfu.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    String uploadThumbnail(MultipartFile multipartFile, String directory) throws IOException;

    void deleteThumbnail(String filename, String directory) throws IOException;

}
