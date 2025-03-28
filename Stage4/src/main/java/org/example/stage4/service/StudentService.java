package org.example.stage4.service;

import org.example.stage4.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    String addStudent(Student student);

    String updateStudent(Student student);

    String deleteStudent(Long id);
}
