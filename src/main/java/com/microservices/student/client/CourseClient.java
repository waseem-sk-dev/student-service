package com.microservices.student.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservices.student.dto.CourseDTO;

@FeignClient(name = "course-service")
public interface CourseClient {

	@GetMapping("/api/courses/{courseId}")
	CourseDTO getCourseById(@PathVariable("courseId") Long id);

	@GetMapping("/api/courses/batch")
	List<CourseDTO> getCoursesByIds(@RequestParam List<Long> ids);

}
