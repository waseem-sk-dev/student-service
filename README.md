📚 Student Service
RESTful Microservice for Student Management
Spring Boot Spring Cloud Java MySQL License

Features • Architecture • Quick Start • API Documentation • Database

</div>
📖 Overview
The Student Service is a robust microservice responsible for managing student-related operations in the Student Management System. Built with Spring Boot and Spring Cloud, it provides comprehensive CRUD operations, student profile management, and seamless integration with other microservices.

Key Responsibilities
📋 Student Management - Create, read, update, and delete student profiles
👥 Enrollment Tracking - Manage student enrollments and academic history
🔍 Student Search - Search and filter students by various criteria
📊 Student Analytics - Track student statistics and status
🔗 Service Integration - Communicate with Course Service for enrollment validation
✨ Features
<table> <tr> <td>
✅ RESTful API Design
✅ Service Discovery (Eureka)
✅ Centralized Configuration
✅ Database Integration (MySQL)
</td> <td>
✅ Input Validation
✅ Exception Handling
✅ API Documentation (Swagger)
✅ Health Monitoring
</td> </tr> </table>
🏗️ Architecture
mermaid

Run
Copy code
graph TB
    A[👥 Client] -->|HTTP Request| B[🌐 API Gateway :8080]
    B -->|Route: /students/**| C[👨‍🎓 Student Service :8082]
    C -->|Register| D[🧭 Eureka Server :8761]
    C -->|Fetch Config| E[🧩 Config Server :8888]
    C -->|Query/Update| F[(MySQL Database)]
    C -->|REST Call| G[📚 Course Service]
    
    style C fill:#2196F3,stroke:#1565C0,stroke-width:3px,color:#fff
    style F fill:#4CAF50,stroke:#2E7D32,stroke-width:2px,color:#fff
Microservices Ecosystem
Service

Port

Description

Student Service

8082

Student management microservice

API Gateway

8080

Routes requests to Student Service

Eureka Server

8761

Service registry and discovery

Config Server

8888

Centralized configuration

Course Service

8083

Course management (for enrollment validation)

🚀 Quick Start
Prerequisites
bash

Run
Copy code
☑ Java 17 or higher
☑ Maven 3.8+
☑ MySQL 8.x
☑ Running Eureka Server (localhost:8761)
☑ Running Config Server (localhost:8888)
Database Setup
sql

Run
Copy code
-- Create database
CREATE DATABASE student_db;

-- Use database
USE student_db;

-- Create students table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    enrollment_date DATE NOT NULL,
    department VARCHAR(50),
    academic_year VARCHAR(10),
    gpa DECIMAL(3,2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_student_id ON students(student_id);
CREATE INDEX idx_email ON students(email);
CREATE INDEX idx_department ON students(department);
Installation
bash

Run
Copy code
# Clone the repository
git clone https://github.com/waseem-sk-dev/student-service.git
cd student-service

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
Docker Deployment
bash

Run
Copy code
# Build Docker image
docker build -t student-service:latest .

# Run container
docker run -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/student_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  student-service:latest
⚙️ Configuration
application.yml
yaml

Run
Copy code
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
    url: jdbc:mysql://localhost:3306/student_db
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
    com.student.service: DEBUG
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG
Environment Variables
bash

Run
Copy code
# Database
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=student_db
export DB_USERNAME=root
export DB_PASSWORD=your_password

# Service Configuration
export SERVER_PORT=8082
export EUREKA_URI=http://localhost:8761/eureka/
export CONFIG_SERVER_URI=http://localhost:8888
📊 Database Schema
Student Entity
sql

Run
Copy code
+-------------------+---------------+------+-----+-------------------+
| Field             | Type          | Null | Key | Default           |
+-------------------+---------------+------+-----+-------------------+
| id                | bigint        | NO   | PRI | AUTO_INCREMENT    |
| student_id        | varchar(20)   | NO   | UNI |                   |
| first_name        | varchar(50)   | NO   |     |                   |
| last_name         | varchar(50)   | NO   |     |                   |
| email             | varchar(100)  | NO   | UNI |                   |
| phone             | varchar(20)   | YES  |     |                   |
| date_of_birth     | date          | YES  |     |                   |
| enrollment_date   | date          | NO   |     |                   |
| department        | varchar(50)   | YES  | MUL |                   |
| academic_year     | varchar(10)   | YES  |     |                   |
| gpa               | decimal(3,2)  | YES  |     | 0.00              |
| is_active         | boolean       | YES  |     | TRUE              |
| created_at        | timestamp     | YES  |     | CURRENT_TIMESTAMP |
| updated_at        | timestamp     | YES  |     | CURRENT_TIMESTAMP |
+-------------------+---------------+------+-----+-------------------+
Sample Data
sql

Run
Copy code
INSERT INTO students (student_id, first_name, last_name, email, phone, date_of_birth, enrollment_date, department, academic_year, gpa)
VALUES 
('STU001', 'John', 'Doe', 'john.doe@example.com', '123-456-7890', '2000-01-15', '2023-09-01', 'Computer Science', '2024-2025', 3.75),
('STU002', 'Jane', 'Smith', 'jane.smith@example.com', '098-765-4321', '2001-05-20', '2023-09-01', 'Mathematics', '2024-2025', 3.90),
('STU003', 'Mike', 'Johnson', 'mike.johnson@example.com', '555-123-4567', '1999-11-10', '2023-09-01', 'English', '2024-2025', 3.20),
('STU004', 'Emily', 'Davis', 'emily.davis@example.com', '777-888-9999', '2002-03-05', '2024-01-15', 'Physics', '2024-2025', 3.85);
📡 API Documentation
Base URL

Run
Copy code
http://localhost:8082/students
Endpoints
1. Get All Students
http

Run
Copy code
GET /students/all
Response:

json
18 lines
Copy code
Download code
Click to expand
[
{
...
2. Get Student by ID
http

Run
Copy code
GET /students/{id}
Example:

bash

Run
Copy code
curl http://localhost:8082/students/1
Response:

json
14 lines
Copy code
Download code
Click to expand
{
"id": 1,
...
3. Get Student by Student ID
http

Run
Copy code
GET /students/studentId/{studentId}
Example:

bash

Run
Copy code
curl http://localhost:8082/students/studentId/STU001
4. Create New Student
http

Run
Copy code
POST /students
Content-Type: application/json
Request Body:

json
12 lines
Copy code
Download code
Click to expand
{
"studentId": "STU005",
...
Example:

bash

Run
Copy code
curl -X POST http://localhost:8082/students \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "STU005",
    "firstName": "Alice",
    "lastName": "Wilson",
    "email": "alice.wilson@example.com",
    "enrollmentDate": "2024-09-01",
    "department": "Computer Science",
    "academicYear": "2024-2025"
  }'
Response: 201 Created

json
10 lines
Copy code
Download code
Click to expand
{
"id": 5,
...
5. Update Student
http

Run
Copy code
PUT /students/{id}
Content-Type: application/json
Request Body:

json
7 lines
Copy code
Download code
Click to expand
{
"firstName": "Alicia",
...
Example:

bash

Run
Copy code
curl -X PUT http://localhost:8082/students/5 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alicia",
    "phone": "444-555-6666",
    "gpa": 3.50
  }'
Response: 200 OK

6. Delete Student
http

Run
Copy code
DELETE /students/{id}
Example:

bash

Run
Copy code
curl -X DELETE http://localhost:8082/students/5
Response: 204 No Content

7. Get Students by Department
http

Run
Copy code
GET /students/department/{department}
Example:

bash

Run
Copy code
curl http://localhost:8082/students/department/Computer%20Science
8. Get Students by Academic Year
http

Run
Copy code
GET /students/academicYear/{academicYear}
Example:

bash

Run
Copy code
curl http://localhost:8082/students/academicYear/2024-2025
9. Get Active Students
http

Run
Copy code
GET /students/active
Returns students where isActive = true

10. Enroll Student in Course
http

Run
Copy code
POST /students/{studentId}/enroll/{courseId}
Example:

bash

Run
Copy code
curl -X POST http://localhost:8082/students/STU001/enroll/1
Response: 200 OK

json
4 lines
Copy code
Download code
Click to expand
{
"message": "Student STU001 enrolled in course 1",
...
11. Update Student GPA
http

Run
Copy code
PUT /students/{studentId}/gpa
Content-Type: application/json
Request Body:

json
3 lines
Copy code
Download code
Click to expand
{
"newGpa": 3.80
...
Example:

bash

Run
Copy code
curl -X PUT http://localhost:8082/students/STU001/gpa \
  -H "Content-Type: application/json" \
  -d '{"newGpa": 3.80}'
🧪 Testing
Unit Tests
bash

Run
Copy code
# Run unit tests
mvn test

# Run with coverage
mvn test jacoco:report
Integration Tests
bash

Run
Copy code
# Run integration tests
mvn verify

# Run specific test class
mvn test -Dtest=StudentServiceIntegrationTest
API Testing with Postman
Import the provided Postman collection:

json
15 lines
Copy code
Download code
Click to expand
{
"info": {
...
📦 Project Structure

Run
Copy code
student-service/
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/com/student/service/
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
│   │   │   │   ├── StudentAlreadyExistsException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── 📂 config/
│   │   │       ├── SwaggerConfig.java
│   │   │       └── WebConfig.java
│   │   └── 📂 resources/
│   │       ├── application.yml
│   │       ├── bootstrap.yml
│   │       └── data.sql
│   └── 📂 test/
│       └── 📂 java/com/student/service/
│           ├── StudentServiceTest.java
│           ├── StudentControllerTest.java
│           └── StudentRepositoryTest.java
├── 📄 pom.xml
├── 📄 Dockerfile
├── 📄 docker-compose.yml
├── 📄 .gitignore
└── 📄 README.md
🐳 Docker Compose
yaml

Run
Copy code
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: student-service-db
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: student_db
      MYSQL_USER: studentuser
      MYSQL_PASSWORD: studentpass
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - student-network

  student-service:
    build: .
    image: student-service:latest
    container_name: student-service
    ports:
      - "8082:8082"
    environment:
      - SPRING
