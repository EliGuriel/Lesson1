package org.example.stage5.service;

import org.example.stage5.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();

    Student addStudent(Student student);

    Student updateStudent(Student student, Long id);

    void deleteStudent(Long id);
}
