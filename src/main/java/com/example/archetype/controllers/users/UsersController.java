package com.example.archetype.controllers.users;

import com.example.archetype.auth.IUserService;
import com.example.archetype.security.PasswordConfig;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class UsersController{

    private final IUserService userService;
    private final PasswordConfig passwordConfig;

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    public UsersController(IUserService userService, PasswordConfig passwordConfig) {
        this.userService = userService;
        this.passwordConfig = passwordConfig;
    }

    @GetMapping(path = "/{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return STUDENTS.stream()
                .filter(student -> studentId.equals(student.getStudentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Student " + studentId + " does not exists"
                ));
    }

    @PostMapping(path = "/register")
    public UserDTO registerUser(@RequestBody UserDTO userDTO) {
        String passCoder = passwordConfig.passwordEncoder().encode(userDTO.getPassword());
        userDTO.setPassword(passCoder);
        userService.registerUser(userDTO);
        return userDTO;
    }
}
