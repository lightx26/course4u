package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByRegistrationId(Long registrationId);
    @Modifying
    @Query("DELETE FROM Document d WHERE d.id IN :deletedDocument AND d.registration.id = :registrationId")
    void deleteAllByIdAndRegistrationId(@Param("deletedDocument") List<Long> deletedDocument, @Param("registrationId") Long registrationId);
}
