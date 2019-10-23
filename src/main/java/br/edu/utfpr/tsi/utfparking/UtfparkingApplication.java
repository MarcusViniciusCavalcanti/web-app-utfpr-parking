package br.edu.utfpr.tsi.utfparking;

import br.edu.utfpr.tsi.utfparking.config.properties.DiskProperties;
import br.edu.utfpr.tsi.utfparking.service.ApplicationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.servlet.ServletContext;

@SpringBootApplication
@EnableConfigurationProperties(DiskProperties.class)
public class UtfparkingApplication implements CommandLineRunner {

    private final ApplicationConfigService applicationConfigService;

    private final ServletContext servletContext;

    @Autowired
    public UtfparkingApplication(ApplicationConfigService applicationConfigService, ServletContext servletContext) {
        this.applicationConfigService = applicationConfigService;
        this.servletContext = servletContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(UtfparkingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        applicationConfigService.setServletContext(servletContext);
        applicationConfigService.loadConfig();
    }
}
