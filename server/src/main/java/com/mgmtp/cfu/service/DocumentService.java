package com.mgmtp.cfu.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    void submitDocument(MultipartFile[] certificates, MultipartFile[] payments, Long id);

}
