package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest;
import com.mgmtp.cfu.dto.registrationdto.RegistrationEnrollDTO;
import com.mgmtp.cfu.exception.BadRequestRuntimeException;
import com.mgmtp.cfu.exception.UnknownErrorException;
import com.mgmtp.cfu.exception.DuplicateCourseException;
import com.mgmtp.cfu.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mgmtp.cfu.dto.RegistrationRequest;
import com.mgmtp.cfu.entity.Registration;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailRegistration(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getDetailRegistration(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Registration> createRegistration(@ModelAttribute RegistrationRequest registrationRequest) {
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
        if (page <= 0)
            page = 1;
        return ResponseEntity.ok(registrationService.getMyRegistrationPage(page, status));
    }
    @PostMapping("/start-learning/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void startLearningCourse(@PathVariable("id") Long registrationId){
        boolean isStarted=registrationService.startLearningCourse(registrationId);
        if(!isStarted)
            throw new UnknownErrorException("Server arise problem.");
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

    @PutMapping("/{id}/finish-learning")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> finishLearning(@PathVariable Long id) {
        registrationService.calculateScore(id);
        return ResponseEntity.status(HttpStatus.OK).body("Saved score successfully!");
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> closeRegistration(@PathVariable Long id, @RequestBody FeedbackRequest feedbackRequest) {
        registrationService.closeRegistration(id, feedbackRequest);
        return ResponseEntity.ok("Close registration successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
    }

    @PostMapping("/{id}/discard")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> discardRegistration(@PathVariable Long id) {
        registrationService.discardRegistration(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createRegistrationFromExistingCourses(@PathVariable Long courseId, @RequestBody RegistrationEnrollDTO registrationEnrollDTO) {
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO);
        return ResponseEntity.ok().build();
    }
}
