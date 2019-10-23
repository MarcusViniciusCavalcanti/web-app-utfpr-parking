package br.edu.utfpr.tsi.utfparking.controller;

import br.edu.utfpr.tsi.utfparking.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final FileService fileService;

    @ResponseBody
    @GetMapping("/user/avatar/{userId}")
    public byte[] getAvatar(@PathVariable("userId") Long id) throws IOException {
        var file = fileService.getFile(id);
        return Files.readAllBytes(file.toPath());
    }
}
