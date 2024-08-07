package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.MailContentUnit;
import com.mgmtp.cfu.dto.documentdto.DocumentDTO;
import com.mgmtp.cfu.entity.Document;
import com.mgmtp.cfu.entity.Notification;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.DocumentType;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.NotificationType;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.enums.Role;
import com.mgmtp.cfu.exception.BadRequestRuntimeException;
import com.mgmtp.cfu.exception.ForbiddenException;
import com.mgmtp.cfu.enums.DocumentStatus;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.repository.DocumentRepository;
import com.mgmtp.cfu.repository.NotificationRepository;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.repository.UserRepository;
import com.mgmtp.cfu.service.DocumentService;
import com.mgmtp.cfu.service.IEmailService;
import com.mgmtp.cfu.util.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mgmtp.cfu.enums.DocumentStatus.PENDING;
import static com.mgmtp.cfu.util.AuthUtils.getCurrentUser;
import static com.mgmtp.cfu.util.DocumentUtils.storageDocument;
import static com.mgmtp.cfu.util.NotificationUtil.createNotification;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final MapperFactory<Document> documentDtoMapperFactory;
    private final RegistrationRepository registrationRepository;
    private final IEmailService emailService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private String documentStorageDir;

    @Value("${course4u.storage.document-directory}")
    public void setDocumentStorageDir(String documentStorageDir) {
        this.documentStorageDir = documentStorageDir;
    }

    @Override
    public List<DocumentDTO> getDocumentsByRegistrationId(Long registrationId) {
        if (!registrationRepository.existsById(registrationId))
            throw new BadRequestRuntimeException("Registration not found.");
        List<Document> list = documentRepository.findAllByRegistrationId(registrationId);
        list = list != null ? list : new ArrayList<>();

        return list.stream().map(getMapper()::toDTO).toList();
    }


    @Override
    public void submitDocument(MultipartFile[] certificates, MultipartFile[] payments, Long id) {
        saveDocuments(certificates, payments, id, RegistrationStatus.DONE);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void resubmit(MultipartFile[] certificates, MultipartFile[] payments, Long id, Long[] deletedDocument) {
        saveDocuments(certificates, payments, id, RegistrationStatus.DOCUMENT_DECLINED);
        if(deletedDocument.length>0)
            deleteDocuments(deletedDocument, id);
    }


    public void saveDocuments(MultipartFile[] certificates, MultipartFile[] payments, Long id, RegistrationStatus status) {
        var user = getCurrentUser();
        var registration = registrationRepository.findById(id).orElseThrow(() ->
                new BadRequestRuntimeException("Registration is not found.")
        );
        if (!registration.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You don't have permission.");
        }
        if (!registration.getStatus().equals(status)) {
            throw new BadRequestRuntimeException("Registration must be "+status.name().toLowerCase()+".");
        }
        storageDocuments(certificates, DocumentType.CERTIFICATE, registration, user);
        storageDocuments(payments, DocumentType.PAYMENT, registration, user);
        if(status.equals(RegistrationStatus.DONE))
            notifyAccountant(user, registration);
        registration.setStatus(RegistrationStatus.VERIFYING);
        registration.setLastUpdated(LocalDateTime.now());
        registrationRepository.save(registration);
    }


    private void notifyAccountant(User user, Registration registration) {
        // TODO: Implement notification logic
        var accountants = userRepository.findAllByRole(Role.ACCOUNTANT);
        var content = "Verification of Course Documents: " + user.getUsername() +
                " has just submitted some document for registration with ID " + registration.getId() +
                ". Please proceed with the verification.";
        List<Notification> notifications = new ArrayList<>();
        accountants.forEach(accountant -> {
            try {
                notifications.add(createNotification(NotificationType.INFORMATION, accountant, content));
                sendNoticedMail(accountant, content);
            } catch (Exception e) {
                log.error(e.getMessage(), e.getCause());
            }
        });
        notificationRepository.saveAll(notifications);

    }

    protected Document verifyDocument(Long id, DocumentStatus documentStatus) {
        var document = documentRepository.findById(id)
                .orElseThrow(() -> new BadRequestRuntimeException("Document not found."));
        document.setStatus(documentStatus);
        return document;
    }

    private void sendNoticedMail(User accountant, String content) {
        List<MailContentUnit> mailContentUnits = List.of(
                EmailUtil.generateTitle("Verification of Course Documents"),
                EmailUtil.updateTitleStyle("Verification of Course Documents"),
                EmailUtil.generateGreeting("Dear {name},", accountant),
                EmailUtil.generateNotifyAccountantContent(content)
        );
        emailService.sendMail(accountant.getEmail(), EmailUtil.generateSubject("Verification of Course Documents"), "email-template.xml", mailContentUnits);
    }

    private DTOMapper<DocumentDTO, Document> getMapper() {
        var documentMapper = documentDtoMapperFactory.getDTOMapper(DocumentDTO.class);
        if (documentMapper.isEmpty()) {
            throw new MapperNotFoundException("No mapper found for DocumentDTO");
        }
        return documentMapper.get();
    }


    private void storageDocuments(MultipartFile[] documents, DocumentType type, Registration registration, User
            user) {
        List<Document> documentList = Arrays.stream(documents)
                .map(document -> {
                    String fileName = storageDocument(type, document, documentStorageDir + "/" + user.getUsername());
                    return Document.builder()
                            .registration(registration)
                            .status(PENDING)
                            .type(type)
                            .url("/api/document-storage/"+ user.getUsername()+"/"+fileName)
                            .build();
                })
                .toList();

        documentRepository.saveAll(documentList);
    }

    private void deleteDocuments(Long[] deletedDocument, Long registrationId) {
        documentRepository.deleteAllByIdAndRegistrationId(List.of(deletedDocument),registrationId);
    }



}
