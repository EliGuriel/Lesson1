package org.example.stage5_2.service;

import org.example.stage5_2.exception.AlreadyExists;
import org.example.stage5_2.exception.NotExists;
import org.example.stage5_2.exception.StudentIdAndIdMismatch;
import org.example.stage5_2.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    List<Student> students = new ArrayList<>(Arrays.asList(
        new Student(1L, "Alice", "Moskovitz", 21.3),
        new Student(2L, "Bob", "Smith", 22.3),
        new Student(3L, "Charlie", "Brown", 23.3),
        new Student(4L, "David", "Miller", 24.3)
    ));

    public List<Student> getAllStudents() {
        return students;
    }

    public Student addStudent(Student student) {
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            // now the exception is thrown, and will be handled in the ControllerAdvice GlobalExceptionHandler
            throw new AlreadyExists("Student with id " + student.getId() + " already exists");
        }
        students.add(student);
        return student;
    }

    public Student updateStudent(Student student, Long id) {
        // check if a student exists
        if (students.stream().noneMatch(s -> s.getId().equals(id))) {
            // now the exception is thrown, and will be handled in the ControllerAdvice GlobalExceptionHandler
            throw new NotExists("Student with id " + student.getId() + " does not exist");
        }
        if (!student.getId().equals(id)) {
            // now the exception is thrown, and will be handled in the ControllerAdvice GlobalExceptionHandler
            throw new StudentIdAndIdMismatch("Student with id " + id + " mismatch with body id " + student.getId());
        }
        students.stream()
            .filter(s -> s.getId().equals(student.getId()))
            .forEach(s -> {
                s.setFirstName(student.getFirstName());
                s.setLastName(student.getLastName());
                s.setAge(student.getAge());
            });
        return student;
    }

    public Student deleteStudent(Long id) {
        // check if a student exists
        if (students.stream().noneMatch(s -> s.getId().equals(id))) {
            // now the exception is thrown, and will be handled in the ControllerAdvice GlobalExceptionHandler
            throw new NotExists ("Student with id " + id + " does not exist");
        }
        students.removeIf(s -> s.getId().equals(id));
        return null;
    }
}
