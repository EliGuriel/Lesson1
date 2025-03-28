package org.example.stage5.service;

import org.example.stage5.exception.AlreadyExists;
import org.example.stage5.exception.NotExists;
import org.example.stage5.exception.StudentIdAndIdMismatch;
import org.example.stage5.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    /**
     * This is a mock database for the sake of this example.
     */
    List<Student> students = new ArrayList<>(Arrays.asList(
        new Student(1L, "Alice", "Moskovitz", 21.3),
        new Student(2L, "Bob", "Smith", 22.3),
        new Student(3L, "Charlie", "Brown", 23.3),
        new Student(4L, "David", "Miller", 24.3)
    ));

    /**
     * This method returns a list of all students.
     *
     * @return List of students
     */
    public List<Student> getAllStudents() {
        return students;
    }

    /**
     * This method adds a new student to the list.
     * @param student the student to be added
     * @return the added student
     */
    public Student addStudent(Student student) {
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new AlreadyExists("Student with id " + student.getId() + " already exists");
        }
        students.add(student);
        return student;
    }

    /**
     * This method updates an existing student.
     * @param student the student to be updated
     * @param id the id of the student to be updated
     * @return the updated student
     */
    public Student updateStudent(Student student, Long id) {
        // Check if the ID parameter matches the student's ID
        if (!student.getId().equals(id)) {
            throw new StudentIdAndIdMismatch("Student with id " + id + " mismatch");
        }

        // Use Stream API to find and update the student in one operation,
        // findFirst return only one element and stops the stream
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

    /**
     * This method deletes a student from the list.
     * @param id the id of the student to be deleted
     */
    public void deleteStudent(Long id) {
        // check if a student exists
        if (students.stream().noneMatch(s -> s.getId().equals(id))) {
            throw new NotExists ("Student with id " + id + " does not exist");
        }
        students.removeIf(s -> s.getId().equals(id));
    }
}
