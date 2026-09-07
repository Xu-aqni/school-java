package com.esercitazione.quizStoria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller 
@RequestMapping("/quiz")
public class WebController {
    
    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/events")
    public String getAllEvents(){
        return "events";
    }

    @GetMapping("/events")
    public String getEvent(){
        return "events";
    }
}
