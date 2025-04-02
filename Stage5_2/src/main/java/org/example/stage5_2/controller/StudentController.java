package org.example.stage5_2.controller;

import jakarta.validation.Valid;
import org.example.stage5_2.model.Student;
import org.example.stage5_2.service.StudentService;
import org.example.stage5_2.service.StudentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentServiceImpl;

    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @GetMapping()
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> studentList = studentServiceImpl.getAllStudents();
        return ResponseEntity.ok(studentList); // 200 OK
    }

    @PostMapping()
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student) {
        // האנוטציה @Valid תגרום לולידציה של אובייקט הסטודנט לפי ההגדרות במודל
        // אם הולידציה תיכשל, תיזרק חריגת MethodArgumentNotValidException
        Student added = studentServiceImpl.addStudent(student);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(added.getId())
                .toUri();

        return ResponseEntity.created(location).body(added); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@Valid @RequestBody Student student, @PathVariable Long id) {
        // גם כאן מתבצעת ולידציה של אובייקט הסטודנט
        Student updated = studentServiceImpl.updateStudent(student, id);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentServiceImpl.deleteStudent(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}