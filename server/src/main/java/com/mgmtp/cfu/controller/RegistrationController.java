package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDetailDTO> getDetailRegistration(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getDetailRegistration(id));
    }

    @GetMapping("/my-registration")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getListOfMyRegistration(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "status", defaultValue = "ALL") String status) {
        if (page <= 0) {
            page = 1;
        }
        return ResponseEntity.ok(registrationService.getMyRegistrationPage(page, status));
    }
}
