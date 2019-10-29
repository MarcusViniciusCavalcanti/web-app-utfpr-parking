package br.edu.utfpr.tsi.utfparking.e2e;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/sql/basic_user.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:/sql/delete_basic_user.sql"})
public class BreadCrumbsTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private WebClient webClient;

    @Before
    public void setup() {

        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .build();

        this.webClient.getOptions().setJavaScriptEnabled(true);
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void check_if_breadcrumb_in_all_user() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost:8080/usuarios");
        HtmlOrderedList breadcrumbs = page.getFirstByXPath("//ol[@class='breadcrumb bc-2']");

        assertTrue(breadcrumbs.asXml().contains("Usuários"));
        assertTrue(breadcrumbs.asXml().contains("Todos"));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void check_if_breadcrumb_new_user() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost:8080/usuarios/novo");
        HtmlOrderedList breadcrumbs = page.getFirstByXPath("//ol[@class='breadcrumb bc-2']");

        assertTrue(breadcrumbs.asXml().contains("Usuários"));
        assertTrue(breadcrumbs.asXml().contains("Novo Usuário"));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void check_if_breadcrumb_edit_user() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost:8080/usuarios/editar/1");
        HtmlOrderedList breadcrumbs = page.getFirstByXPath("//ol[@class='breadcrumb bc-2']");

        assertTrue(breadcrumbs.asXml().contains("Usuários"));
        assertTrue(breadcrumbs.asXml().contains("Vinicius"));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void check_if_breadcrumbs_config() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost:8080/configuracoes");

        HtmlOrderedList breadcrumbs = page.getFirstByXPath("//ol[@class='breadcrumb bc-2']");

        var config = breadcrumbs.asXml().contains("Configurações");
        var setter = breadcrumbs.asXml().contains("Ajustar");

        assertThat(config, Matchers.notNullValue());
        assertThat(setter, Matchers.notNullValue());
    }
}
