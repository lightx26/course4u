package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.MailContentUnit;
import com.mgmtp.cfu.service.IEmailService;
import com.mgmtp.cfu.service.IResourceService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@Slf4j
@RequiredArgsConstructor
@Setter
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender emailSender;
    private final IResourceService resourceService;
    @Value("${course4u.mail.enable}")
    private Boolean isEnableEmailSender;
    @Value("${spring.mail.username}")
    private String emailAddress;


    public void sendMessage(String to, String subject, String templateName, List<MailContentUnit> mailContentUnits) {
        if (!isEnableEmailSender)
            return;
        var resource = resourceService.loadResourceByName("email/template/" + templateName);
        var content = generateContentForWelcomeMail(mailContentUnits, resource);
        if (Objects.isNull(content))
            return;
        MimeMessage message = emailSender.createMimeMessage();
        sendMail(message, content, to, subject);
    }

    private void sendMail(MimeMessage message, String content, String to, String subject) {
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                    helper.setFrom(emailAddress);
                    helper.setTo(to);
                    helper.setSubject(subject);
                    helper.setText(content, true);
                    emailSender.send(message);
                } catch (MessagingException ex) {
                    log.error(ex.getMessage(), ex);
                }
            });
            executorService.shutdown();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private String generateContentForWelcomeMail(List<MailContentUnit> mailContentUnits, Resource resource) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(resource.getFile());
            log.info(document.getName() + mailContentUnits.get(0).getTag());
            for (MailContentUnit mailContentUnit : mailContentUnits)
                modifyContent(document, mailContentUnit.getId(), mailContentUnit.getContent(), mailContentUnit.getHref(), mailContentUnit.getTag());
            return document.asXML();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    private void modifyContent(Document document, String id, String newText, String newHref, String tag) {
        Element element = findLinkById(document.getRootElement(), id, tag);
        if (Objects.nonNull(newText) && !newText.trim().isEmpty())
            element.setText(newText);
        if (Objects.nonNull(newHref) && !newHref.trim().isEmpty())
            element.attribute("href").setValue(newHref);

    }

    private Element findLinkById(Element root, String id, String tag) {
        return (Element) root.selectSingleNode("//" + tag + "[@id='" + id + "']");
    }

}
