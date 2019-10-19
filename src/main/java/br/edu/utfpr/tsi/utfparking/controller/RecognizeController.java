package br.edu.utfpr.tsi.utfparking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecognizeController {

    @GetMapping("/reconhecimentos")
    public String recognize() {
        return "recognize/recognize-notifications";
    }

}
