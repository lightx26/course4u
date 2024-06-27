INSERT INTO "Course" ("Name", "Link", "Platform", "ThumbnailUrl", "TeacherName", "CreatedDate", "Status")
VALUES ('Java', 'https://www.udemy.com/course/java-the-complete-java-developer-course/', 'Udemy',
        'https://img-a.udemycdn.com/course/240x135/533682_c10c_4.jpg', 'Tim Buchalka','2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy',
        'https://img-a.udemycdn.com/course/240x135/907624_2d2e_6.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('React', 'https://www.udemy.com/course/react-the-complete-guide-incl-redux/', 'Udemy',
        'https://img-a.udemycdn.com/course/240x135/1362070_b9a1_2.jpg', 'Maximilian Schwarzmüller', '2021-01-01',
        'AVAILABLE');

INSERT INTO "Registration" ("CourseId", "RegisterDate", "Status", "StartDate", "EndDate", "Score", "Duration")
VALUES (1, '2021-01-01', 'CANCELLED', '2021-01-01', '2021-01-31', 20, 15),
       (2, '2021-01-02', 'DONE', '2021-01-01', '2021-01-31', 30, 13),
       (3, '2021-01-04', 'VERIFIED', '2021-01-01', '2021-01-31', 40, 200);