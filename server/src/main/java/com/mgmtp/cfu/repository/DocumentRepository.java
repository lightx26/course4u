package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByRegistrationId(Long registrationId);
}
