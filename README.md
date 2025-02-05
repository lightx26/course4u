# 📌 Course4U - Web Application - ReactJS & Java Spring Boot

## 📖 Giới Thiệu

Course4U này là một ứng dụng web full-stack được phát triển bằng **ReactJS** cho frontend và **Spring Boot** cho backend. Ứng dụng cung cấp các chức năng như quản lý người dùng, quản lí khóa học, quản lí đơn đăng ký, quản lí chứng chỉ và bằng chứng thanh toán, xác thực, và tích hợp API.
Có 3 role chính: User, Admin và Accountant.

## ✨ Công Nghệ Sử Dụng

### **Frontend** (ReactJS)

-   ReactJS với TypeScript
-   UI: Shadcn, Ant Design, TailwindCSS
-   State Management: Redux Toolkit
-   Giao tiếp API: Axios
-   Build Tool: Vite.

### **Backend** (Spring Boot)

-   Spring Boot 3.x (Java 17/21)
-   Spring Security (JWT Auth)
-   Spring Data JPA (PostgreSQL)
-   RESTful API
-   Swagger API Documentation
-   Persistence: Liquibase
-   Unit-Test: Spock framework + Groovy
-   Code Coverage: Jacoco
-   Build tool: Gradle

### **DevOps & CI/CD**

-   Docker & Docker Compose
-   Jenkins Pipeline
-   Nginx Reverse Proxy
-   Traefik

### **Version Control**

-   Git
-   BitBucket as hosting server
-   Auth: JWT, OAuth2 with our simple solution.

---

## 📸 Hình Ảnh Minh Họa

<img src="./client/public/course/default-course-thumnail.svg" alt="Homepage" width="800"/>
Update Later...
---

## 📂 Cấu Trúc Thư Mục

```
Update later...
```

---

## 🚀 Cài Đặt & Chạy Dự Án

### 1️⃣ Cài Đặt Frontend

```bash
cd client
npm install
npm run dev
```

### 2️⃣ Cài Đặt Backend

```bash
cd server
./mvnw spring-boot:run
```

### 3️⃣ Chạy bằng Docker

```bash
docker-compose up --build
```

---

## 📌 API Documentation (Swagger)

Sau khi khởi chạy backend, truy cập [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) để xem tài liệu API.

---

## 🔥 Các Tính Năng Chính

✅ Authentication & Authorization (JWT)
✅ Quản lý người dùng (CRUD API)
✅ Giao diện thân thiện, hiện đại với TailwindCSS & Ant Design
✅ Kết nối Frontend & Backend qua Axios
✅ CI/CD với Jenkins & Docker

---

## Đối tượng sử dụng

-   Các công ty đang muốn khuyến khích nhân viên phát triển kĩ năng cá nhân, quản lý đơn đăng ký khóa học online và minh chứng hoàn thành khóa học (hóa đơn, chứng chỉ) để hoàn tiền cho các nhân viên đã hoàn thành khóa học. Đồng thời đây cũng là 1 nền tảng để các nhân viên chia sẻ chất lượng khóa học, recommend những khóa học hay để mọi người cùng phát triển.

## Các chức năng:

-   Đăng nhập/đăng ký
-   Đăng ký khóa học online ở trên nền tảng thứ 3 hoặc ngay trên nền tảng Course4U. Đối với các khóa học thuộc các nền tảng nối tiếng **Udemy**, **Coursera**, **Linkedln**,.. thì bạn có thể tạo đơn đăng ký, hoặc khóa học đơn giản bằng đường link dẫn đến, thông tin sẽ tự được fill.
-   Quản lý Course (các khóa học), Registration (Đơn đăng ký khóa học), Chứng chỉ, hóa đơn thanh toán khóa học nhằm mục đích nhận hoàn tiền.
-   Theo dõi tiến độ hoàn thành khóa học.
-   Sau khi hoàn thành khóa học, có thể nộp minh chứng để yêu cầu hoàn tiền cho khóa học.

---

```mermaid
graph TB
    User((User))
    Admin((Admin))
    Accountant((Accountant))

    subgraph "Course4U System"
        subgraph "Frontend Container"
            ClientApp["Client Application<br>React + TypeScript"]

            subgraph "Frontend Components"
                AuthComponents["Auth Components<br>React"]
                CourseComponents["Course Components<br>React"]
                UserComponents["User Components<br>React"]
                SharedComponents["Shared Components<br>React"]
                ReduxStore["State Management<br>Redux + Redux Saga"]
                RouterComponent["Router<br>React Router"]
                ModalComponents["Modal System<br>React"]
            end
        end

        subgraph "Backend Container"
            SpringApp["Spring Boot Application<br>Java"]

            subgraph "Core Components"
                SecurityModule["Security Module<br>Spring Security + JWT"]
                Controllers["REST Controllers<br>Spring MVC"]
                Services["Business Services<br>Spring Services"]
                Repositories["Data Access Layer<br>Spring Data JPA"]
                Mappers["DTO Mappers<br>ModelMapper"]
                Schedulers["Scheduler<br>Spring Scheduler"]
            end
        end

        subgraph "Storage"
            FileStorage["File Storage<br>Local FileSystem"]
            Database[("Database<br>H2/PostgreSQL")]
        end
    end

    %% User Interactions
    User -->|"Accesses"| ClientApp
    Admin -->|"Manages"| ClientApp
    Accountant -->|"Processes"| ClientApp

    %% Frontend Component Relations
    ClientApp -->|"Uses"| AuthComponents
    ClientApp -->|"Uses"| CourseComponents
    ClientApp -->|"Uses"| UserComponents
    ClientApp -->|"Uses"| SharedComponents
    ClientApp -->|"Uses"| ReduxStore
    ClientApp -->|"Uses"| RouterComponent
    ClientApp -->|"Uses"| ModalComponents

    %% Frontend to Backend Communication
    ClientApp -->|"API Requests"| SpringApp

    %% Backend Component Relations
    SpringApp -->|"Uses"| SecurityModule
    SpringApp -->|"Routes to"| Controllers
    Controllers -->|"Delegates to"| Services
    Services -->|"Uses"| Repositories
    Services -->|"Uses"| Mappers
    SpringApp -->|"Schedules"| Schedulers

    %% Data Storage Relations
    Repositories -->|"Persists data"| Database
    Services -->|"Manages"| FileStorage

    %% Component Interactions
    SecurityModule -->|"Authenticates"| Controllers
    ReduxStore -->|"State Updates"| CourseComponents
    ReduxStore -->|"State Updates"| UserComponents
    RouterComponent -->|"Routes to"| CourseComponents
    RouterComponent -->|"Routes to"| UserComponents
    RouterComponent -->|"Routes to"| AuthComponents
```
