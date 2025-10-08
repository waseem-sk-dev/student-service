package com.microservices.student.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microservices.student.client.CourseClient;
import com.microservices.student.dto.CourseDTO;
import com.microservices.student.dto.StudentResponse;
import com.microservices.student.entity.Student;
import com.microservices.student.repository.StudentRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentService {
	private final StudentRepository studentRepository;
	private final CourseClient courseClient;

	@CircuitBreaker(name = "courseService", fallbackMethod = "getStudentFallback")
	@Retry(name = "courseService")
	public StudentResponse getStudentWithCourses(Long studentId) {
		log.info("Fetching student with ID: {}", studentId);
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		List<CourseDTO> courses = new ArrayList<>();
		if (!student.getEnrolledCourseIds().isEmpty()) {
			courses = courseClient.getCoursesByIds(student.getEnrolledCourseIds());
		}
		return StudentResponse.builder().id(student.getId()).name(student.getName()).email(student.getEmail())
				.age(student.getAge()).courses(courses).build();
	}

	// Fallback method
	private StudentResponse getStudentFallback(Long studentId, Exception e) {
		log.error("Fallback triggered for student:{}", studentId, e);
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		return StudentResponse.builder().id(student.getId()).name(student.getName()).email(student.getEmail())
				.age(student.getAge()).courses(new ArrayList<>()).build();
	}

	public Student createStudent(Student student) {
		return studentRepository.save(student);
	}

	public void enrollInCourse(Long studentId, Long courseId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		if (!student.getEnrolledCourseIds().contains(courseId)) {
			student.getEnrolledCourseIds().add(courseId);
			studentRepository.save(student);
		}
	}

}
