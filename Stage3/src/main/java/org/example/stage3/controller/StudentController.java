package org.example.stage3.controller;


import org.example.stage3.model.Student;
import org.example.stage3.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentServiceImpl;

    public StudentController(StudentService studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @GetMapping("/getAllStudents")
    public List<Student> getAllStudents() {
        return studentServiceImpl.getAllStudents();
    }
}
