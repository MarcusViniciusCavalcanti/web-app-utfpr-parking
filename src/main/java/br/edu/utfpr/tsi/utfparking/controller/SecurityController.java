package br.edu.utfpr.tsi.utfparking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/login")
    public String login() {
        return "security/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

    @GetMapping("/access-denied-public")
    public String accessDeniedPublic() {
        return "/error/access-denied";
    }
}
