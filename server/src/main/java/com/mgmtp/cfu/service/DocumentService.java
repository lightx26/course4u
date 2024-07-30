package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.documentdto.DocumentDTO;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    void submitDocument(MultipartFile[] certificates, MultipartFile[] payments, Long id);

    List<DocumentDTO> getDocumentsByRegistrationId(Long registrationId);


    void resubmit(MultipartFile[] certificates, MultipartFile[] payments, Long id, Long[] deletedDocument);
}
