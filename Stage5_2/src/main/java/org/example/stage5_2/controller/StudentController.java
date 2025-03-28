package org.example.stage5_2.controller;

import jakarta.validation.Valid;
import org.example.stage5_2.model.Student;
import org.example.stage5_2.service.StudentService;
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
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student) {
        // האנוטציה @Valid תגרום לולידציה של אובייקט הסטודנט לפי ההגדרות במודל
        // אם הולידציה תיכשל, תיזרק חריגת MethodArgumentNotValidException
        Student added = studentService.addStudent(student);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(added.getId())
                .toUri();

        return ResponseEntity.created(location).body(added); // 201 Created
    }

    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Student> updateStudent(@Valid @RequestBody Student student, @PathVariable Long id) {
        // גם כאן מתבצעת ולידציה של אובייקט הסטודנט
        Student updated = studentService.updateStudent(student, id);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}