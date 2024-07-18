package com.mgmtp.cfu.entity;

import com.mgmtp.cfu.enums.DocumentStatus;
import com.mgmtp.cfu.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "`Document`")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Id`")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "`RegistrationId`")
    private Registration registration;

    @Column(name = "`Url`")
    private String url;

    @Column(name = "`Status`")
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @Column(name = "`Type`")
    @Enumerated(EnumType.STRING)
    private DocumentType type;




}
