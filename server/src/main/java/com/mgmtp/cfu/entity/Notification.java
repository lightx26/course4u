package com.mgmtp.cfu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mgmtp.cfu.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
@Table(name = "`Notification`")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Id`")
    Long id;

    @Column(name = "`Content`")
    String content;

    @Column(name = "`CreatedAt`")
    ZonedDateTime createdAt;

    @Column(name = "`Type`")
    @Enumerated(EnumType.STRING)
    NotificationType type;

    @Column(name = "`Seen`")
    Boolean seen;

    @ManyToOne
    @JoinColumn(name = "`UserId`", referencedColumnName = "`Id`")
    @JsonBackReference
    User user;
}
