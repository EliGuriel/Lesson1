package org.example.stage6.service;

import org.example.stage6.exception.NotFoundException;
import org.example.stage6.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    // a list of students to simulate a database
    List<Student> students = new ArrayList<>(Arrays.asList(
            new Student(1L, "Alice", "Moskovitz", 21.3),
            new Student(2L, "Bob", "Smith", 22.3),
            new Student(3L, "Charlie", "Brown", 23.3),
            new Student(4L, "David", "Miller", 24.3)
    ));

    public List<Student> getAllStudents() {
        return students;
    }

    public Optional<Student> getStudentById(Long id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public Student addStudent(Student student) {
        // check if a student with the same ID already exists
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new IllegalArgumentException("Student with id " + student.getId() + " already exists");
        }

        /*
        here we can add more validations that cannot be done at the annotation level, for example,
         if (student.getFirstName().equals(student.getLastName())) or check for uniqueness of first and last name
         we can also check for other business rules */
        validateStudent(student);

        students.add(student);
        return student;
    }

    public Student updateStudent(Student student) {
        // check if a student with the same ID already exists
        if (students.stream().noneMatch(s -> s.getId().equals(student.getId()))) {
            throw new NotFoundException("Student with id " + student.getId() + " does not exist");
        }

        // check for other validations that cannot be done at the annotation level
        validateStudent(student);

        // delete the existing student and add the updated student
        students.removeIf(s -> s.getId().equals(student.getId()));
        students.add(student);

        return student;
    }

    public void deleteStudent(Long id) {
        // check if a student with the given ID exists
        if (students.stream().noneMatch(s -> s.getId().equals(id))) {
            throw new NotFoundException("Student with id " + id + " does not exist");
        }

        students.removeIf(s -> s.getId().equals(id));
    }

    // additional validations that cannot be done at the annotation level
    private void validateStudent(Student student) {
        /* more validations that cannot be done at the annotation level
              for example, if first name equals last name, or check for uniqueness of first and last name
         */
        if (student.getFirstName() != null && student.getLastName() != null) {
            if (student.getFirstName().equals(student.getLastName())) {
                throw new IllegalArgumentException("First name and last name cannot be identical");
            }
        }

        // check for uniqueness (excluding the ID)
        boolean duplicateNameExists = students.stream()
                .filter(s -> !s.getId().equals(student.getId())) // exclude the student being updated
                .anyMatch(s -> s.getFirstName().equals(student.getFirstName()) &&
                        s.getLastName().equals(student.getLastName()));

        if (duplicateNameExists) {
            throw new IllegalArgumentException("A student with the same first and last name already exists");
        }
    }
}