package br.edu.utfpr.tsi.utfparking.e2e;

import br.edu.utfpr.tsi.utfparking.UtfparkingApplication;
import br.edu.utfpr.tsi.utfparking.data.AccessCardRepository;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = UtfparkingApplication.class)
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/sql/basic_user.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:/sql/delete_basic_user.sql"})
public class UserTest {

    private WebClient webClient;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccessCardRepository accessCardRepository;

    @Value("${file.root}")
    private String directoryRoot;

    @Value("${file.avatar}")
    private String directoryAvatar;

    private String  url = "http://localhost:8080/usuarios";

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .build();

    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_loaded_avatar_by_user() throws IOException {
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
        HtmlPage page = webClient.getPage("http://localhost:8080/reconhecimentos");

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
                fail();
            }
        }, () -> {
            throw new EntityNotFoundException();
        });
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_return_page_user() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("totalElements", Matchers.is(3)));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_return_page_user_when_call_with_params_search() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/all/search/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("totalElements", Matchers.is(1)));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_view_all_user_in_datatable() throws IOException {
        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(10000);
        HtmlTable userTable = page.getFirstByXPath("//table[@id='users_table']");

        var headers = userTable.getHeader();
        var htmlTableBody = userTable.getBodies().get(0);

        assertThat(headers.asText(), Matchers.stringContainsInOrder(List.of("ID", "Nome", "Login", "Placa", "Modelo do Carro", "Tipo", "Desbloqueada", "Ativa")));
        assertThat(htmlTableBody.getRows().get(0).asText(), Matchers.stringContainsInOrder(List.of("Vinicius", "vinicius_user")));
        assertThat(htmlTableBody.getRows().get(1).asText(), Matchers.stringContainsInOrder(List.of("Vinicius", "vinicius_admin")));
        assertThat(htmlTableBody.getRows().get(2).asText(), Matchers.stringContainsInOrder(List.of("Vinicius", "vinicius_operator")));
    }

    @Test
    @WithUserDetails(value = "vinicius_user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_redirect_to_access_denied_when_user_role_is_user() throws Exception {
        var url = "http://localhost:8080/usuarios";
        HtmlPage page = webClient.getPage(url);

        assertThat(page.asXml().contains("Error 403"), Matchers.is(true));
        assertThat(page.asXml().contains("Acesso Negado!"), Matchers.is(true));
    }

    @Test
    @WithUserDetails(value = "vinicius_operator", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_redirect_to_access_denied_when_user_role_is_operator() throws Exception {
        var url = "http://localhost:8080/usuarios";
        HtmlPage page = webClient.getPage(url);

        assertThat(page.asXml().contains("Error 403"), Matchers.is(true));
        assertThat(page.asXml().contains("Acesso Negado!"), Matchers.is(true));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_redirect_to_not_found() throws IOException {
        HtmlPage page = webClient.getPage(url + "/editar/1000000000");

        assertThat(page.asXml().contains("Error 404"), Matchers.is(true));
        assertThat(page.asXml().contains("Recurso não disponivel!"), Matchers.is(true));
    }
}
