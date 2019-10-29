package br.edu.utfpr.tsi.utfparking.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/not-found")
    public String notFound() {
        return "/error/not-found";
    }

}
