<div align="center">

👨‍🎓 Student Service
RESTful Microservice for Student Management
Features • Architecture • Quick Start • API Documentation • Database

</div>

📖 Overview
The Student Service is a robust microservice primarily responsible for managing student registration, profiles, and academic records within the Student Management System. Built with Spring Boot and Spring Cloud, it provides comprehensive CRUD operations and seamless integration with other microservices.

Key Responsibilities
👤 Student Management - Create, read, update, and delete student profiles

📚 Academic Tracking - Track student's major, department, and enrollment status

✅ Validation Service - Verify student existence and status for enrollment processes

🔍 Student Discovery - Search and filter students by ID, name, or department

🔗 Service Integration - Provide data to Course Service for enrollment validation

✨ Features
<table>
<tr>
<td>

✅ RESTful API Design

✅ Service Discovery (Eureka)

✅ Centralized Configuration

✅ Database Integration (MySQL)

</td>
<td>

✅ Input Validation

✅ Exception Handling

✅ API Documentation (Swagger)

✅ Health Monitoring

</td>
</tr>
</table>

🏗️ Architecture
Code snippet

graph TB
    A[👥 Client] -->|HTTP Request| B[🌐 API Gateway :8080]
    B -->|Route: /students/**| C[👨‍🎓 Student Service :8082]
    C -->|Register| D[🧭 Eureka Server :8761]
    C -->|Fetch Config| E[🧩 Config Server :8888]
    C -->|Query/Update| F[(MySQL Database)]
    C -->|REST Call| G[📚 Course Service]
    
    style C fill:#4CAF50,stroke:#2E7D32,stroke-width:3px,color:#fff
    style F fill:#2196F3,stroke:#1565C0,stroke-width:2px,color:#fff
Microservices Ecosystem
Service	Port	Description
Student Service	8082	Student management microservice
API Gateway	8080	Routes requests to Student Service
Eureka Server	8761	Service registry and discovery
Config Server	8888	Centralized configuration
Course Service	8083	Course management (consumes student validation)

Export to Sheets
🚀 Quick Start
Prerequisites
Bash

☑ Java 17 or higher
☑ Maven 3.8+
☑ MySQL 8.x
☑ Running Eureka Server (localhost:8761)
☑ Running Config Server (localhost:8888)
Database Setup
SQL

-- Create database
CREATE DATABASE student_management_db;

-- Use database
USE student_management_db;

-- Create students table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL, -- The public ID used for enrollment
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50),
    major VARCHAR(50),
    enrollment_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Active', -- Active, Inactive, Graduated
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_student_id ON students(student_id);
CREATE INDEX idx_department ON students(department);
CREATE INDEX idx_status ON students(status);
Installation
Bash

# Clone the repository
git clone https://github.com/waseem-sk-dev/student-service.git
cd student-service

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
Docker Deployment
Bash

# Build Docker image
docker build -t student-service:latest .

# Run container
docker run -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/student_management_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  student-service:latest
⚙️ Configuration
application.yml
YAML

# ============================
# Server Configuration
# ============================
server:
  port: 8082

# ============================
# Spring Application
# ============================
spring:
  application:
    name: student-service

  # ============================
  # Database Configuration
  # ============================
  datasource:
    url: jdbc:mysql://localhost:3306/student_management_db
    username: root
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

  # ============================
  # Config Server
  # ============================
  config:
    import: optional:configserver:http://localhost:8888

# ============================
# Eureka Client
# ============================
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    instance-id: ${spring.application.name}:${server.port}

# ============================
# Actuator Endpoints
# ============================
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always

# ============================
# Logging
# ============================
logging:
  level:
    com.student.management: DEBUG
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG
📊 Database Schema
Student Entity
SQL

+-------------------+---------------+------+-----+-------------------+
| Field             | Type          | Null | Key | Default           |
+-------------------+---------------+------+-----+-------------------+
| id                | bigint        | NO   | PRI | AUTO_INCREMENT    |
| student_id        | varchar(20)   | NO   | UNI |                   |
| first_name        | varchar(100)  | NO   |     |                   |
| last_name         | varchar(100)  | NO   |     |                   |
| email             | varchar(100)  | NO   | UNI |                   |
| department        | varchar(50)   | YES  | MUL |                   |
| major             | varchar(50)   | YES  |     |                   |
| enrollment_date   | date          | NO   |     | CURRENT_DATE      |
| status            | varchar(20)   | YES  | MUL | Active            |
| created_at        | timestamp     | YES  |     | CURRENT_TIMESTAMP |
| updated_at        | timestamp     | YES  |     | CURRENT_TIMESTAMP |
+-------------------+---------------+------+-----+-------------------+
Sample Data
SQL

INSERT INTO students (student_id, first_name, last_name, email, department, major, enrollment_date, status)
VALUES 
('101', 'Alice', 'Smith', 'alice.s@university.edu', 'Computer Science', 'Software Engineering', '2023-09-01', 'Active'),
('102', 'Bob', 'Johnson', 'bob.j@university.edu', 'Mathematics', 'Applied Math', '2023-09-01', 'Active'),
('103', 'Charlie', 'Brown', 'charlie.b@university.edu', 'English', 'Creative Writing', '2023-01-15', 'Active');
📡 API Documentation
Base URL
http://localhost:8082/students
Endpoints
1. Get All Students
HTTP

GET /students/all
Response: (Abridged)

JSON

[
  {
    "id": 1,
    "studentId": "101",
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice.s@university.edu",
    "department": "Computer Science",
    "major": "Software Engineering",
    "status": "Active"
  }
]
2. Get Student by Internal ID
HTTP

GET /students/{id}
Example:

Bash

curl http://localhost:8082/students/1
3. Get Student by Student ID (For Enrollment Validation)
HTTP

GET /students/sid/{studentId}
Example:

Bash

curl http://localhost:8082/students/sid/101
4. Create New Student
HTTP

POST /students
Content-Type: application/json
Request Body:

JSON

{
  "studentId": "104",
  "firstName": "Diana",
  "lastName": "Prince",
  "email": "diana.p@university.edu",
  "department": "Physics",
  "major": "Astrophysics",
  "enrollmentDate": "2024-09-01"
}
5. Update Student
HTTP

PUT /students/{id}
Content-Type: application/json
Request Body: (Partial update)

JSON

{
  "major": "Theoretical Physics",
  "status": "Active"
}
6. Delete Student
HTTP

DELETE /students/{id}
Response: 204 No Content

7. Get Students by Department
HTTP

GET /students/department/{department}
Example:

Bash

curl http://localhost:8082/students/department/Computer%20Science
8. Check Student Status
HTTP

GET /students/sid/{studentId}/status
Response: 200 OK

JSON

{
  "studentId": "101",
  "status": "Active"
}
🧪 Testing
Unit Tests
Bash

# Run unit tests
mvn test

# Run with coverage
mvn test jacoco:report
API Testing with Postman
Import the provided Postman collection:

JSON

{
  "info": {
    "name": "Student Service API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Get All Students",
      "request": {
        "method": "GET",
        "url": "http://localhost:8082/students/all"
      }
    }
  ]
}
📦 Project Structure
student-service/
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/com/student/management/
│   │   │   ├── StudentServiceApplication.java
│   │   │   ├── 📂 controller/
│   │   │   │   └── StudentController.java
│   │   │   ├── 📂 service/
│   │   │   │   ├── StudentService.java
│   │   │   │   └── StudentServiceImpl.java
│   │   │   ├── 📂 repository/
│   │   │   │   └── StudentRepository.java
│   │   │   ├── 📂 entity/
│   │   │   │   └── Student.java
│   │   │   ├── 📂 dto/
│   │   │   │   ├── StudentRequestDto.java
│   │   │   │   └── StudentResponseDto.java
│   │   │   ├── 📂 exception/
│   │   │   │   ├── StudentNotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── 📂 config/
│   │   │       └── SwaggerConfig.java
│   │   └── 📂 resources/
│   │       ├── application.yml
│   │       ├── bootstrap.yml
│   │       └── data.sql
│   └── 📂 test/
│       └── 📂 java/com/student/management/
│           ├── StudentServiceTest.java
│           └── StudentControllerTest.java
├── 📄 pom.xml
└── 📄 Dockerfile
...
🐳 Docker Compose
YAML

version: '3.8'

services:
  mysql-student:
    image: mysql:8.0
    container_name: student-service-db
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: student_management_db
      MYSQL_USER: studentuser
      MYSQL_PASSWORD: studentpass
    ports:
      - "3306:3306" # Use default 3306 for student DB
    volumes:
      - student-mysql-data:/var/lib/mysql
    networks:
      - student-network

  student-service:
    build: .
    image: student-service:latest
    container_name: student-service
    ports:
      - "8082:8082"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql-student:3306/student_management_db
      - SPRING_DATASOURCE_USERNAME=studentuser
      - SPRING_DATASOURCE_PASSWORD=studentpass
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
    depends_on:
      - mysql-student
    networks:
      - student-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8082/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

volumes:
  student-mysql-data:

networks:
  student-network:
    driver: bridge
🔒 Security
Input Validation
Java

@NotBlank(message = "Student ID is required")
@Size(min = 3, max = 20, message = "Student ID must be between 3 and 20 characters")
private String studentId;

@NotBlank(message = "First name is required")
private String firstName;

@Email(message = "Email must be valid")
private String email;

@PastOrPresent(message = "Enrollment date cannot be in the future")
private LocalDate enrollmentDate;
Exception Handling
Java

@ExceptionHandler(StudentNotFoundException.class)
public ResponseEntity<ErrorResponse> handleStudentNotFound(StudentNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}
🛡️ Best Practices & Additional Resources
(Content for Best Practices, Monitoring, Troubleshooting, Additional Resources, License, and Author sections would be identical or structurally similar to the Course Service README, with name/path changes where necessary.)

👨‍💻 Author
<div align="center">

Waseem Shaikh
Backend Developer | Java • Spring Boot • Microservices

</div>

<div align="center">

⭐ Star this repository if you find it helpful!

Made with ❤️ by Waseem Shaikh

</div>












Tools

