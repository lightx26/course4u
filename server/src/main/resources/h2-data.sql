INSERT INTO "Course" ("Name", "Link", "Platform", "Level", "ThumbnailUrl", "TeacherName", "CreatedDate", "Status")
VALUES ('Java', 'https://www.udemy.com/course/java-the-complete-java-developer-course/', 'Udemy', 'INTERMEDIATE',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'Tim Buchalka', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'ADVANCED',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'BEGINNER',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'BEGINNER',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'BEGINNER',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'BEGINNER',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'ADVANCED',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'ADVANCED',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'ADVANCED',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'INTERMEDIATE',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'INTERMEDIATE',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy', 'INTERMEDIATE',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('React', 'https://www.udemy.com/course/react-the-complete-guide-incl-redux/', 'Udemy', 'INTERMEDIATE',
        'https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpgg', 'Maximilian Schwarzmüller', '2021-01-01',
        'AVAILABLE');

insert into "User"("Username", "Password", "Email", "Role","AvatarUrl","Telephone")
values ('user','$2a$10$8O/b4SGusmdgMuZFIv7JaOQXx1sQ.ISrffmXjGgC2b10S1ML.4ity','user@mgm-tp.com','USER','https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg','123456789'),
       ('admin','$2a$10$bhBp0hDRVdW1A6RMKt0k0.m4709.So5X8.3GhmSOwNzcptfJOm0FW','admin@mgm-tp.com','ADMIN','https://i.ytimg.com/vi/M85iQzbuLMs/maxresdefault.jpg','123456789');

INSERT INTO "Registration" ("CourseId", "UserId","RegisterDate", "Status", "StartDate", "EndDate", "Score", "Duration")
VALUES (1, 1,'2021-01-01', 'DISCARDED', '2021-01-01', '2021-01-31', 20, 15),
       (2, 1,'2021-01-02', 'DONE', '2021-01-02', '2021-01-31', 30, 13),
       (3, 1,'2021-01-04', 'VERIFIED', '2021-01-04', '2021-01-31', 40, 200),
       (1, 1,'2021-01-01', 'DOCUMENT_DECLINED', '2021-01-01', '2021-01-31', 20, 15),
       (2, 1,'2021-01-02', 'VERIFYING', '2021-01-02', '2021-01-31', 30, 13),
       (3, 1,'2021-01-04', 'APPROVED', '2021-01-04', '2021-01-31', 40, 200),
       (1, 1,'2021-01-01', 'CLOSED', '2021-01-01', '2021-01-31', 20, 15),
       (2, 1,'2021-01-02', 'SUBMITTED', '2021-01-02', '2021-01-31', 30, 13),
       (3, 1,'2021-01-04', 'DRAFT', '2021-01-04', '2021-01-31', 40, 200),
       (3, 1,'2021-01-04', 'DECLINED', '2021-01-04', '2021-01-31', 40, 200),
       (3, 1,'2021-01-04', 'VERIFIED', '2021-01-04', '2021-01-31', 40, 200);
INSERT INTO "Registration" ("CourseId", "RegisterDate", "Status", "StartDate", "EndDate", "Score", "Duration", "UserId",
                            "DurationUnit")
VALUES (1, '2021-01-02', 'DISCARDED', '2021-01-02', '2021-01-31', 20, 15, 1, 'WEEK'),
       (2, '2021-01-03', 'DONE', '2021-01-03', '2021-01-31', 30, 13, 1, 'WEEK'),
       (3, '2021-01-04', 'VERIFIED', '2021-01-04', '2021-01-31', 40, 200, 1, 'DAY'),
       (4, '2021-01-05', 'DISCARDED', '2021-01-05', '2021-01-31', 20, 15, 1, 'WEEK'),
       (5, '2021-01-06', 'DONE', '2021-01-06', '2021-01-31', 30, 13, 1, 'WEEK'),
       (6, '2021-01-02', 'VERIFIED', '2021-01-02', '2021-01-31', 40, 200, 1, 'DAY'),
       (7, '2021-01-01', 'DISCARDED', '2021-01-01', '2021-01-31', 20, 15, 1, 'WEEK'),
       (8, '2021-01-03', 'DONE', '2021-01-03', '2021-01-31', 30, 13, 1, 'WEEK'),
       (9, '2021-01-04', 'VERIFIED', '2021-01-04', '2021-01-31', 40, 200, 1, 'DAY'),
       (10, '2021-01-02', 'DISCARDED', '2021-01-02', '2021-01-31', 20, 15, 1, 'WEEK'),
       (11, '2021-01-02', 'DONE', '2021-01-02', '2021-01-31', 30, 13, 1, 'WEEK'),
       (12, '2021-01-04', 'VERIFIED', '2021-01-04', '2021-01-31', 40, 200, 1, 'DAY')
;


INSERT INTO "Category"("Name")
VALUES ('IT'), ('Science'), ('Physics');

INSERT INTO "Category_Course"("CourseId", "CategoryId")
VALUES (1, 1), (1, 2), (1, 3);
