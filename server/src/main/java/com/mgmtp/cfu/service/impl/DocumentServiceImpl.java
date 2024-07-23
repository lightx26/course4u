package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.MailContentUnit;
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
import com.mgmtp.cfu.repository.DocumentRepository;
import com.mgmtp.cfu.repository.NotificationRepository;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.repository.UserRepository;
import com.mgmtp.cfu.service.DocumentService;
import com.mgmtp.cfu.service.IEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mgmtp.cfu.enums.DocumentStatus.PENDING;
import static com.mgmtp.cfu.util.AuthUtils.getCurrentUser;
import static com.mgmtp.cfu.util.Constant.ACCOUNTANT_NOTIFICATION_EMAIL_TEMPLATE_NAME;
import static com.mgmtp.cfu.util.DocumentUtils.storageDocument;
import static com.mgmtp.cfu.util.NotificationUtil.createNotification;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final RegistrationRepository registrationRepository;
    private final IEmailService emailService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    private String documentStorageDir;
    @Value("${course4u.vite.frontend.url}")
    private String clientUrl;

    @Value("${course4u.storage.document-directory}")
    public void setDocumentStorageDir(String documentStorageDir) {
        this.documentStorageDir = documentStorageDir;
    }


    @Override
    public void submitDocument(MultipartFile[] certificates, MultipartFile[] payments, Long id) {
        var registration = registrationRepository.findById(id).orElseThrow(() ->
                new BadRequestRuntimeException("Registration is not found.")
        );

        var user = getCurrentUser();
        if (!registration.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You don't have permission.");
        }
        if (!registration.getStatus().equals(RegistrationStatus.DONE)) {
            throw new BadRequestRuntimeException("Registration must be done.");
        }
        storageDocuments(certificates, DocumentType.CERTIFICATE, registration, user);
        storageDocuments(payments, DocumentType.PAYMENT, registration, user);
        notifyAccountant(user,registration);
        registration.setStatus(RegistrationStatus.VERIFYING);
        registrationRepository.save(registration);
    }

    private void notifyAccountant(User user, Registration registration) {
        // TODO: Implement notification logic
        var accountants=userRepository.findAllByRole(Role.ACCOUNTANT);
        var content="Verification of Course Documents: " + user.getUsername() +
        " has just submitted some document for registration with ID " + registration.getId() +
                ". Please proceed with the verification.";
        List<Notification> notifications=new ArrayList<>();
        accountants.forEach(accountant->{
            try{
                notifications.add(createNotification(NotificationType.INFORMATION,accountant,content));
                sendNoticedMail(accountant,content);
            }catch (Exception e){
                log.error(e.getMessage(),e.getCause());
            }
        });
        notificationRepository.saveAll(notifications);

    }

    private void sendNoticedMail(User accountant, String content) {
        var title="Verification of Course Documents";
        List<MailContentUnit> mailContentUnits=List.of(
                MailContentUnit.builder().id("notification_title").content(title).tag("div").build(),
                MailContentUnit.builder().id("client_url").href(clientUrl).tag("a").build(),
                MailContentUnit.builder().id("greeting").content("Dear "+ (accountant.getFullName()!=null?accountant.getFullName():accountant.getUsername())).tag("div").build(),
                MailContentUnit.builder().id("content").content(content).tag("div").build()
        );
        emailService.sendMessage(accountant.getEmail(),title,ACCOUNTANT_NOTIFICATION_EMAIL_TEMPLATE_NAME, mailContentUnits );

    }


    private void storageDocuments(MultipartFile[] documents, DocumentType type, Registration registration, User user) {
        List<Document> documentList = Arrays.stream(documents)
                .map(document -> {
                    String fileUrl = storageDocument(type, document, documentStorageDir+"/"+user.getUsername());
                    return Document.builder()
                            .registration(registration)
                            .status(PENDING)
                            .type(type)
                            .url(fileUrl.substring(13))
                            .build();
                })
                .toList();

        documentRepository.saveAll(documentList);
    }

}
