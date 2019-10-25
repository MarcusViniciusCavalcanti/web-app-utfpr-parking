package br.edu.utfpr.tsi.utfparking.web.controller;

import br.edu.utfpr.tsi.utfparking.service.FileService;
import br.edu.utfpr.tsi.utfparking.service.UserService;
import br.edu.utfpr.tsi.utfparking.web.content.InputUser;
import br.edu.utfpr.tsi.utfparking.web.content.PageUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final FileService fileService;

    private final UserService userService;

    @ResponseBody
    @GetMapping("/user/avatar/{userId}")
    public byte[] getAvatar(@PathVariable("userId") Long id) throws IOException {
        var file = fileService.getFile(id);
        return Files.readAllBytes(file.toPath());
    }

    @GetMapping("/usuarios")
    public ModelAndView recognize(InputUser inputUser) {
        var model = new ModelAndView("users/index");
        model.addObject("user", inputUser);

        return model;
    }

    @GetMapping("/users/all")
    public ResponseEntity<Page<PageUser>> getPageUser(Pageable pageable) {
        return ResponseEntity.ok(userService.getPage(pageable));
    }

    @GetMapping("/users/all/search/{search}")
    public ResponseEntity<Page<PageUser>> getPageUser(@PathVariable("search") String search, Pageable pageable) {
        return ResponseEntity.ok(userService.getPageBySearch(search, pageable));
    }
}
