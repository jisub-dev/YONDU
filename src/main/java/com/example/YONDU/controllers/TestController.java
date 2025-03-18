package com.example.YONDU.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/memebr")
    @PreAuthorize("hasRole('MEMBER') or hasRole('TRAINER') or hasRole('MANAGER')")
    public String userAccess() {
        return "Member Content.";
    }

    @GetMapping("/trainer")
    @PreAuthorize("hasRole('TRAINER')")
    public String trainerAccess() {
        return "Trainer Board.";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public String managerAccess() {
        return "Manager Board.";
    }
}}
