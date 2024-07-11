package com.mgmtp.cfu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`CourseReview`")
public class CourseReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Id`")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "`CourseId`", referencedColumnName = "`Id`")
    @JsonBackReference
    private Course course;

    @ManyToOne
    @JoinColumn(name = "`UserId`", referencedColumnName = "`Id`")
    @JsonBackReference
    private User user;

    @Column(name = "`Rating`")
    private Integer rating;

    @Column(name = "`Comment`")
    private String comment;

    @Column(name = "`CreatedDate`")
    private LocalDateTime createdDate;
}
