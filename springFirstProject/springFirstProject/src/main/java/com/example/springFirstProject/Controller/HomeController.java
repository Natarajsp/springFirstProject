package com.example.springFirstProject.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping({"/hello", "/"})
    public String welcomeLearner () {
        return "Welcome to the Spring Boot Learning Course";
    }
}
