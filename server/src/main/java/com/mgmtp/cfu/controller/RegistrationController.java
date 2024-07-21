package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest;
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.exception.DuplicateCourseException;
import com.mgmtp.cfu.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.mgmtp.cfu.dto.RegistrationRequest;
import com.mgmtp.cfu.entity.Registration;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDetailDTO> getDetailRegistration(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getDetailRegistration(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Registration> createRegistration(@ModelAttribute RegistrationRequest registrationRequest     ) {
        try {
            registrationService.createRegistration(registrationRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DuplicateCourseException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/my-registration")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getListOfMyRegistration(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "status", defaultValue = "ALL") String status) {
        if (page <= 0) {
            page = 1;
        }
        return ResponseEntity.ok(registrationService.getMyRegistrationPage(page, status));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> approveRegistration(@PathVariable Long id) {
        registrationService.approveRegistration(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decline")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> declineRegistration(@PathVariable Long id,@RequestBody FeedbackRequest feedbackRequest) {
        registrationService.declineRegistration(id,feedbackRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
    }

}
