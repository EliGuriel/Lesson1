package org.example.stage5.controller;

import org.example.stage5.exception.AlreadyExists;
import org.example.stage5.exception.NotExists;
import org.example.stage5.exception.StudentIdAndIdMismatch;
import org.example.stage5.model.Student;
import org.example.stage5.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> studentList = studentService.getAllStudents();
        return ResponseEntity.ok(studentList); // 200 OK
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Object> addStudent(@RequestBody Student student) {
        try {
            Student added = studentService.addStudent(student);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(added.getId())
                    .toUri();

            return ResponseEntity.created(location).body(added); // 201 Created
        } catch (AlreadyExists e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable Long id) {
        try {
            Student updated = studentService.updateStudent(student, id);
            return ResponseEntity.ok(updated); // 200 OK
        } catch (NotExists | StudentIdAndIdMismatch e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (NotExists e) {
            return ResponseEntity.notFound().build(); //  notFound()
        }
    }
}