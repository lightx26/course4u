package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.MailContentUnit;

import java.util.List;

public interface IEmailService {
    void sendMail(String to, String subject, String templateName, List<MailContentUnit> mailContentUnits);
}
