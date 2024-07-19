package com.mgmtp.cfu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mgmtp.cfu.enums.Gender;
import com.mgmtp.cfu.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;



@Entity
@Table(name = "`User`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Id`")
    private Long id;
    @Column(name = "`Username`")
    private String username;
    @Column(name = "`Password`")
    private String password;
    @Column(name = "`FullName`")
    private String fullName;
    @Column(name = "`Email`")
    private String email;
    @Column(name = "`Telephone`")
    private String telephone;
    @Column(name = "`AvatarUrl`")
    private String avatarUrl;
    @Column(name = "`DateOfBirth`")
    private LocalDate dateOfBirth;
    @Column(name = "`Role`")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "`Gender`")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Registration> registrations;
}
