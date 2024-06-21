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
    private String name;
    private String link;
    private String platform;
    @Column(name = "thumbnailUrl")
    private String thumbnailUrl;
    @Column(name = "teacherName")
    private String teacherName;
    @Column(name = "createdDate")
    private Date createdDate;
    private String status;
}