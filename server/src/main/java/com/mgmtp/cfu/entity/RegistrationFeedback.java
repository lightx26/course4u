package com.mgmtp.cfu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
@Table(name = "`RegistrationFeedback`")
public class RegistrationFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Id`")
    Long id;

    @Column(name = "`Comment`")
    String comment;

    @Column(name="`CreatedDate`")
    LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "`RegistrationId`", referencedColumnName = "`Id`")
    @JsonBackReference
    private Registration registration;

    @ManyToOne
    @JoinColumn(name = "`UserId`", referencedColumnName = "`Id`")
    @JsonBackReference
    private User user;
}
