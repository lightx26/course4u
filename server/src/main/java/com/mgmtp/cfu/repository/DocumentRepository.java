package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
