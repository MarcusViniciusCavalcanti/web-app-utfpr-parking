package br.edu.utfpr.tsi.utfparking.controller;

import br.edu.utfpr.tsi.utfparking.UtfparkingApplication;
import br.edu.utfpr.tsi.utfparking.data.AccessCardRepository;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = UtfparkingApplication.class)
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/sql/basic_user.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:/sql/delete_basic_user.sql"})
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private WebClient webClient;

    @Autowired
    private AccessCardRepository accessCardRepository;

    @Value("${file.root}")
    private String directoryRoot;

    @Value("${file.avatar}")
    private String directoryAvatar;

    @Before
    public void setup() {
        this.webClient = MockMvcWebClientBuilder
                .webAppContextSetup(webApplicationContext).build();

        this.webClient.getOptions().setJavaScriptEnabled(true);
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_loaded_avatar_by_user() throws IOException {
        var url = "http://localhost:8080/configuracoes";
        HtmlPage page = webClient.getPage(url);

        accessCardRepository.findByUsername("vinicius_admin").ifPresentOrElse(admin -> {

            assertTrue(page.asXml().contains(String.format("/user/avatar/%d", admin.getUser().getId())));
            HtmlImage image = page.getFirstByXPath("//img[@id='avatar_user']");

            try {
                var tempFile = Files.createTempFile("image", "teste").toFile();
                image.saveAs(tempFile);

                try (InputStream inputStream = new FileInputStream(tempFile)) {
                    var bytes = new byte[inputStream.available()];

                    inputStream.read(bytes);

                    var testImage = Base64Utils.encodeToString(bytes);

                    var imageSaveInDisk = Files.readAllBytes(Path.of(directoryRoot + directoryAvatar + "/" + admin.getId() + ".png"));

                    var concreteImage = Base64Utils.encodeToString(imageSaveInDisk);

                    assertThat(testImage, Matchers.is(concreteImage));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, () -> {
            throw new EntityNotFoundException();
        });
    }

    @Test
    @WithUserDetails(value = "vinicius_operator", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_loaded_avatar_default_when_avatar_is_not_found() throws IOException {
        var url = "http://localhost:8080/reconhecimentos";
        HtmlPage page = webClient.getPage(url);

        accessCardRepository.findByUsername("vinicius_operator").ifPresentOrElse(operator -> {
            assertTrue(page.asXml().contains(String.format("/user/avatar/%d", operator.getUser().getId())));
            HtmlImage image = page.getFirstByXPath("//img[@id='avatar_user']");

            try {
                var tempFile = Files.createTempFile("image", "teste").toFile();
                image.saveAs(tempFile);

                try (InputStream inputStream = new FileInputStream(tempFile)) {
                    var bytes = new byte[inputStream.available()];

                    inputStream.read(bytes);

                    var testImage = Base64Utils.encodeToString(bytes);

                    var imageSaveInDisk = Files.readAllBytes(Path.of(directoryRoot + directoryAvatar + "/default.png"));

                    var concreteImage = Base64Utils.encodeToString(imageSaveInDisk);

                    assertThat(testImage, Matchers.is(concreteImage));
                }
            } catch (IOException e) {
                e.printStackTrace();
                assertTrue(false);
            }
        }, () -> {
            throw new EntityNotFoundException();
        });
    }
}
