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
public class StudentServiceImpl implements StudentService {

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
        // Check if the ID parameter matches the student's ID
        if (!student.getId().equals(id)) {
            // Exception is thrown and will be handled in the ControllerAdvice GlobalExceptionHandler
            throw new StudentIdAndIdMismatch("Student with id " + id + " mismatch with body id " + student.getId());
        }

        // Use Stream API to find and update the student in one operation
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .map(existingStudent -> {
                    existingStudent.setFirstName(student.getFirstName());
                    existingStudent.setLastName(student.getLastName());
                    existingStudent.setAge(student.getAge());
                    return existingStudent;
                })
                .orElseThrow(() -> new NotExists("Student with id " + id + " does not exist"));
    }

    public void deleteStudent(Long id) {
        // check if a student exists
        if (students.stream().noneMatch(s -> s.getId().equals(id))) {
            // now the exception is thrown, and will be handled in the ControllerAdvice GlobalExceptionHandler
            throw new NotExists ("Student with id " + id + " does not exist");
        }
        students.removeIf(s -> s.getId().equals(id));
    }
}
