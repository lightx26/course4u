package com.mgmtp.cfu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "`Course`")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Id`")
    private Long id;
    @Column(name = "`Name`")
    private String name;
    @Column(name = "`Link`")
    private String link;
    @Column(name = "`Platform`")
    private String platform;
    @Column(name = "`Level`")
    @Enumerated(EnumType.STRING)
    private CourseLevel level;
    @Column(name = "`ThumbnailUrl`")
    private String thumbnailUrl;
    @Column(name = "`TeacherName`")
    private String teacherName;
    @Column(name = "`CreatedDate`")
    private LocalDate createdDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "`Status`")
    private CourseStatus status;
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<Registration> registrations;
}