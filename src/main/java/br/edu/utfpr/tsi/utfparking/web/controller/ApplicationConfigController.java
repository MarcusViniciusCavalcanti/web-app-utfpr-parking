package br.edu.utfpr.tsi.utfparking.web.controller;

import br.edu.utfpr.tsi.utfparking.models.dtos.ApplicationConfigDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.ApplicationConfig;
import br.edu.utfpr.tsi.utfparking.service.ApplicationConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationConfigController {

    private final ApplicationConfigService applicationConfigService;

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/configuracoes")
    public ModelAndView recognize(ApplicationConfigDTO config, HttpServletRequest context) {
        var configuration = (ApplicationConfig) context.getServletContext().getAttribute("config");
        var model = new ModelAndView("config/show");

        config.setId(configuration.getId());
        config.setModeSystem(configuration.getModeSystem());
        config.setIp(configuration.getIp());

        model.addObject("config", config);

        return model;
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/configuracoes/salvar")
    public ModelAndView save(@Valid ApplicationConfigDTO config, HttpServletRequest request, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return new ModelAndView("config/show");
        }

        applicationConfigService.setServletContext(request.getServletContext());
        applicationConfigService.save(config);

        return new ModelAndView("redirect:/reconhecimentos");
    }
}
