package com.backend.backend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/testing")
@RestController
public class Testcontroller {
    @GetMapping("/apptesting")
    public Object TestAPI() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "test this");
        return response;
    }
}