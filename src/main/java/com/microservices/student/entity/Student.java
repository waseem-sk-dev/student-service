package com.microservices.student.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

@Column(nullable=false)
private String name;

@Column(nullable=false,unique=true)
private String email;

@Column(nullable=false)
private int age;

@ElementCollection
@CollectionTable(name="student-course",
joinColumns=@JoinColumn(name="student_id"))
@Column(name="course_id")
@Builder.Default
private List<Long> enrolledCourseIds=new ArrayList<>();
}
