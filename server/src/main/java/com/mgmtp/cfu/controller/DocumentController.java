package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.exception.BadRequestRunTimeException;
import com.mgmtp.cfu.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.mgmtp.cfu.util.Constant.DOCUMENT_SIZE_LIMIT;
import static com.mgmtp.cfu.util.DocumentUtils.isAllowedExtension;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {
    private final DocumentService documentService;
    @PostMapping("/registrations/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void submitDocument(@RequestParam("certificate") MultipartFile[] certificates,
                                                 @RequestParam("payment") MultipartFile[] payments,
                               @PathVariable("id") Long id) {
        Stream.concat(Arrays.stream(certificates), Arrays.stream(payments)).forEach(multipartFile -> {
                    if (multipartFile.getSize() > DOCUMENT_SIZE_LIMIT)
                        throw new MaxUploadSizeExceededException(DOCUMENT_SIZE_LIMIT);
                    if(!isAllowedExtension(multipartFile.getOriginalFilename()))
                        throw new BadRequestRunTimeException("File extension can be uploaded: PDF, DOCX, JPG, JPEG, PNG");
        }

        );
        if(id==null)
            throw new BadRequestRunTimeException("Id must be not null");
        if (certificates.length == 0 || payments.length == 0) {
            throw new BadRequestRunTimeException("User must submit document completely.");
        }
        documentService.submitDocument(certificates, payments,id);

    }
}
