package com.mgmtp.cfu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CoursePlatform;
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
    @Column(name="`Id`")
    private Long id;

    @Column(name = "`Name`")
    private String name;

    @Column(name = "`Link`")
    private String link;

    @Column(name = "`Platform`")
    @Enumerated(EnumType.STRING)
    private CoursePlatform platform;

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

    @ManyToMany
    @JoinTable(name = "`Category_Course`",
               joinColumns = @JoinColumn(name = "`CourseId`"),
               inverseJoinColumns = @JoinColumn(name = "`CategoryId`"))
    private Set<Category> categories;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Registration> registrations;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CourseReview> courseReviews;

}
