package com.mobile.base.controller.oauth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {
    @GetMapping(value = {"/", "/home"})
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the home page");
    }
}
