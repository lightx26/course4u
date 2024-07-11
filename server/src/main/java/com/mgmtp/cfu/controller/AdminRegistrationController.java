package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/registrations")
public class AdminRegistrationController {
    private final RegistrationService registrationService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getRegistrationForAdmin(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "status", defaultValue = "all") String status)
    {
        if (page <= 0){
            return ResponseEntity.badRequest().body("Page number must be greater than 0");
        }

        return (status.equalsIgnoreCase("all") || status.isEmpty()) ?
                ResponseEntity.ok(registrationService.getAllRegistrations(page)):
                ResponseEntity.ok(registrationService.getRegistrationByStatus(page, status));
    }
}
