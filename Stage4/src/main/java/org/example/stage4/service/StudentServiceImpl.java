package org.example.stage4.service;

import org.example.stage4.model.Student;
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

    public String addStudent(Student student) {
        // check if a student already exists
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            return ("Student with id " + student.getId() + " already exists");
        }
        getAllStudents().add(student);
        return "Student added successfully";
    }

    public String updateStudent(Student student) {
        return students.stream()
                .filter(s -> s.getId().equals(student.getId()))
                .findFirst()
                .map(existingStudent -> {
                    existingStudent.setFirstName(student.getFirstName());
                    existingStudent.setLastName(student.getLastName());
                    existingStudent.setAge(student.getAge());
                    return "Student updated successfully";
                })
                .orElse("Student with id " + student.getId() + " does not exist");
    }


    public String deleteStudent(Long id) {
        // check if a student exists
        if (students.stream().noneMatch(s -> s.getId().equals(id))) {
            return ("Student with id " + id + " does not exist");
        }
        students.removeIf(s -> s.getId().equals(id));
        return "Student deleted successfully";
    }
}
