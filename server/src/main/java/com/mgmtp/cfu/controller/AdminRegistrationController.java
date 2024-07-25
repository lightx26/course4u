package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams;
import com.mgmtp.cfu.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.mgmtp.cfu.util.RegistrationValidator.validateRegistrationOverviewParams;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/registrations")
@PreAuthorize("hasAnyRole({'ROLE_ADMIN','ROLE_ACCOUNTANT'})")
public class AdminRegistrationController {
    private final RegistrationService registrationService;

    @GetMapping
    public ResponseEntity<?> getRegistrationsForAdmin(
            @ModelAttribute RegistrationOverviewParams params,
            @RequestParam(value = "page", defaultValue = "1") int page
    )
    {
        validateRegistrationOverviewParams(params);
        return ResponseEntity.ok(registrationService.getRegistrations(params, page));
    }
}
