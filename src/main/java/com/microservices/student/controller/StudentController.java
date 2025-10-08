package com.microservices.student.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.student.dto.StudentResponse;
import com.microservices.student.entity.Student;
import com.microservices.student.service.StudentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
	private final StudentService studentService;

	@GetMapping("{id}")
	public ResponseEntity<StudentResponse> getStudent(@PathVariable Long id) {
		return ResponseEntity.ok(studentService.getStudentWithCourses(id));
	}

	@PostMapping
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
	}

	@PostMapping("/{studentId}/courses/{courseId}")
	public ResponseEntity<String> enrollInCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
		studentService.enrollInCourse(studentId, courseId);
		return ResponseEntity.ok("Enrolled successfully");
	}

}
