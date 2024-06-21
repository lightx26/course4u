package com.mgmtp.cfu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Link")
    private String link;
    @Column(name = "Platform")
    private String platform;
    @Column(name = "ThumbnailUrl")
    private String thumbnailUrl;
    @Column(name = "TeacherName")
    private String teacherName;
    @Column(name = "CreatedDate")
    private Date createdDate;
    @Column(name = "Status")
    private String status;
}