package org.example.stage6.controller;

import org.example.stage6.exception.NotFoundException;
import org.example.stage6.model.ErrorResponse;
import org.example.stage6.model.Student;
import org.example.stage6.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);  /* Status: 200 OK */
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudentById(@Valid @PathVariable Long id) {
        // here we use Optional to handle the case when the student is not found
        Optional<Student> studentOpt = studentService.getStudentById(id);
        return
                /* if the student is found, return 200 OK with the student data */
                studentOpt.<ResponseEntity<Object>>map(student -> ResponseEntity.ok().body(student))
                        /* or else return 404 Not Found with an error message */
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse("Not Found", "Student with id " + id + " does not exist")));
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Object> addStudent(@Valid @RequestBody Student student) {
        try {
            Student added = studentService.addStudent(student);

            /* it is customary to return the URI-"Location" of the newly created resource
             at response header */
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(added.getId())
                    .toUri();

            return ResponseEntity.created(location).body(added);  /* Status: 201 Created */
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()  /* Status: 400 Bad Request */
                    .body(new ErrorResponse("Invalid student data", e.getMessage()));
        }
    }

    /*
    best practice to pass the ID in the URL path, not in the request body
        see file : "06_Passing_ID_as_Param_URL_WHY.md"
    */
    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        try {
            // check if the ID in the request body matches the path ID,
            if (student.getId() != null && !student.getId().equals(id)) {
                return ResponseEntity
                        .badRequest()           /* Status: 400 Bad Request */
                        .body(new ErrorResponse("ID mismatch",
                                "ID in the path (" + id + ") doesn't match ID in the request body (" + student.getId() + ")"));
            }

            // ensure the correct ID is set before processing
            student.setId(id);

            Student updated = studentService.updateStudent(student);
            return ResponseEntity.ok(updated);      /* Status: 200 OK */
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)  /* Status: 404 Not Found */
                    .body(new ErrorResponse("Student not found", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()                   /* Status: 400 Bad Request */
                    .body(new ErrorResponse("Invalid data", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<Object> deleteStudent(@Valid @PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();  /* Status: 204 No Content, (success) */
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)  /* Status: 404 Not Found */
                    .body(new ErrorResponse("Student not found", e.getMessage()));
        }
    }
}