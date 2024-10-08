package com.mgmtp.cfu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mgmtp.cfu.enums.DurationUnit;
import com.mgmtp.cfu.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "`Registration`")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Id`")
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "`Status`")
    RegistrationStatus status;

    @Column(name = "`StartDate`")
    ZonedDateTime startDate;

    @Column(name = "`EndDate`")
    ZonedDateTime endDate;

    @Column(name = "`Score`")
    Integer score;

    @Column(name = "`RegisterDate`")
    LocalDate registerDate;

    @Column(name = "`Duration`")
    Integer duration;

    @ManyToOne
    @JoinColumn(name = "`CourseId`", referencedColumnName = "`Id`")
    @JsonBackReference
    Course course;

    @Column(name = "`LastUpdated`")
    LocalDateTime lastUpdated;

    @Column(name = "`DurationUnit`")
    @Enumerated(EnumType.STRING)
    DurationUnit durationUnit;

    @ManyToOne
    @JoinColumn(name = "`UserId`")
    User user;

    @OneToMany(mappedBy = "registration",cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<RegistrationFeedback> registrationFeedbacks;

}
