package com.authcentral.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Authservice is running";
    }

    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }
}
