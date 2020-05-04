package br.edu.utfpr.tsi.utfparking.web.controller;

import br.edu.utfpr.tsi.utfparking.service.FileService;
import br.edu.utfpr.tsi.utfparking.service.RoleServices;
import br.edu.utfpr.tsi.utfparking.service.UserService;
import br.edu.utfpr.tsi.utfparking.web.content.InputUser;
import br.edu.utfpr.tsi.utfparking.web.content.PageUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final FileService fileService;

    private final UserService userService;

    private final RoleServices roleServices;

    @ResponseBody
    @GetMapping("/user/avatar/{userId}")
    public byte[] getAvatar(@PathVariable("userId") Long id) throws IOException {
        var file = fileService.getFile(id);
        return Files.readAllBytes(file.toPath());
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ModelAndView recognize(InputUser inputUser) {
        var model = new ModelAndView("users/index");
        model.addObject("user", inputUser);

        return model;
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios/novo")
    public ModelAndView newUser(InputUser inputUser) {
        var roles = roleServices.getAllRoles();
        var model = new ModelAndView("users/form");

        model.addObject("user", inputUser);
        model.addObject("roles", roles);

        return model;
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios/editar/{id}")
    public ModelAndView editUser(@PathVariable("id") Long id) {
        try {
            var inputUser = userService.getUserById(id);
            var roles = roleServices.getAllRoles();
            var model = new ModelAndView("users/form");

            model.addObject("user", inputUser);
            model.addObject("roles", roles);
            return model;
        } catch (EntityNotFoundException e) {
            return new ModelAndView("redirect:/error/not-found");
        }

    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/usuarios/novo")
    public ModelAndView saveNewUser(InputUser inputUser, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("users/form");
        }

        userService.saveNewUser(inputUser);
        return new ModelAndView("redirect:/usuarios");
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/usuarios/editar")
    public ModelAndView editUser(InputUser inputUser, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("users/form");
        }

        userService.editUser(inputUser);
        return new ModelAndView("redirect:/usuarios");
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/all")
    public ResponseEntity<Page<PageUser>> getPageUser(Pageable pageable) {
        return ResponseEntity.ok(userService.getPage(pageable));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/all/search/{search}")
    public ResponseEntity<Page<PageUser>> getPageUser(@PathVariable("search") String search, Pageable pageable) {
        return ResponseEntity.ok(userService.getPageBySearch(search, pageable));
    }
}
