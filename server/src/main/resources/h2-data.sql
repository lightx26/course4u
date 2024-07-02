INSERT INTO "Course" ("Name", "Link", "Platform", "ThumbnailUrl", "TeacherName", "CreatedDate", "Status")
VALUES ('Java', 'https://www.udemy.com/course/java-the-complete-java-developer-course/', 'Udemy',
        'https://img-a.udemycdn.com/course/240x135/533682_c10c_4.jpg', 'Tim Buchalka','2021-01-01', 'AVAILABLE'),
       ('Spring Boot', 'https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/', 'Udemy',
        'https://img-a.udemycdn.com/course/240x135/907624_2d2e_6.jpg', 'In28Minutes Official', '2021-01-01', 'AVAILABLE'),
       ('React', 'https://www.udemy.com/course/react-the-complete-guide-incl-redux/', 'Udemy',
        'https://img-a.udemycdn.com/course/240x135/1362070_b9a1_2.jpg', 'Maximilian Schwarzmüller', '2021-01-01',
        'AVAILABLE');

INSERT INTO "Registration" ("CourseId", "RegisterDate", "Status", "StartDate", "EndDate", "Score", "Duration")
VALUES (1, '2021-01-01', 'DISCARDED', '2021-01-01', '2021-01-31', 20, 15),
       (2, '2021-01-02', 'DONE', '2021-01-01', '2021-01-31', 30, 13),
       (3, '2021-01-04', 'VERIFIED', '2021-01-01', '2021-01-31', 40, 200);
insert into "User"("Username", "Password", "Email", "Role")
values ('user','$2a$10$8O/b4SGusmdgMuZFIv7JaOQXx1sQ.ISrffmXjGgC2b10S1ML.4ity','user@mgm-tp.com','USER'),
       ('admin','$2a$10$bhBp0hDRVdW1A6RMKt0k0.m4709.So5X8.3GhmSOwNzcptfJOm0FW','admin@mgm-tp.com','ADMIN');
--user user_password
--admin admin_password