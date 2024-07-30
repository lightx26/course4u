package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.service.DocumentService;
import com.mgmtp.cfu.util.DocumentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Arrays;

import static com.mgmtp.cfu.util.RequestValidator.validateId;


@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/registrations/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void submitDocument(@RequestParam(value = "certificate") MultipartFile[] certificates,
                               @RequestParam(value="payment") MultipartFile[] payments,
                               @PathVariable("id") Long id) {
        payments=payments!=null?payments:new MultipartFile[]{};
        certificates=certificates!=null?certificates:new MultipartFile[]{};
        DocumentUtils.validate(certificates,payments,id) ;
        documentService.submitDocument(certificates, payments, id);

    }

    @GetMapping("/registration/{registrationId}")
    public ResponseEntity<?> getDocument(@PathVariable("registrationId") Long registrationId) {
        validateId(registrationId, "registration id");
        return ResponseEntity.ok(documentService.getDocumentsByRegistrationId(registrationId));
    }
    @PostMapping("/registrations/{id}/resubmit-document")
    public void resubmitDocument(@RequestParam(value = "certificate",required = false) MultipartFile[] certificates,
                                 @RequestParam(value = "payment",required = false) MultipartFile[] payments,
                                 @PathVariable("id") Long id, @RequestParam("deleted_documents") String[] deletedDocument){
        payments=payments!=null?payments:new MultipartFile[]{};
        certificates=certificates!=null?certificates:new MultipartFile[]{};
        DocumentUtils.validate(certificates,payments,id);
        Long[] deletedDocumentLongArray = new Long[deletedDocument.length];
        for (int i=0; i<deletedDocument.length; i++) {
            deletedDocumentLongArray[i] = Long.parseLong(deletedDocument[i]);
        }
        documentService.resubmit(certificates,payments,id,deletedDocumentLongArray);
    }



}
