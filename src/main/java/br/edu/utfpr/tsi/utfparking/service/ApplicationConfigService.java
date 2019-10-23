package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.data.ApplicationConfigRepository;
import br.edu.utfpr.tsi.utfparking.models.dtos.ApplicationConfigDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationConfigService implements ServletContextAware {

    private ServletContext servletContextAware;

    private final ApplicationConfigRepository applicationConfigRepository;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContextAware = servletContext;
    }

    public void loadConfig() {
        var applicationConfig = applicationConfigRepository.findById(1L)
                .orElseGet(() -> applicationConfigRepository.save(new ApplicationConfig(1L, "disable")));

        this.servletContextAware.setAttribute("config", applicationConfig);
    }

    public void save(ApplicationConfigDTO config) {
        applicationConfigRepository.findById(1L).ifPresent(applicationConfig -> {
            applicationConfig.setModeSystem(config.getModeSystem());
            applicationConfig.setIp(config.getIp());

            applicationConfigRepository.save(applicationConfig);

            this.servletContextAware.setAttribute("config", applicationConfig);
        });

    }
}
