package br.edu.utfpr.tsi.utfparking.e2e;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
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
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/sql/basic_user.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:/sql/delete_basic_user.sql"})
public class SidebarTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private WebClient webClient;

    @Before
    public void setup() {
        var mockMvc = MockMvcBuilders
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
    public void check_if_sideBar_item_active_config() throws Exception {
        var url = "http://localhost:8080/configuracoes";
        HtmlPage page = webClient.getPage(url);

        HtmlListItem liConfig = page.getFirstByXPath("//li[@id='config']");
        HtmlListItem liSetter = page.getFirstByXPath("//li[@id='config_setter']");

        var openedClass = liConfig.getAttribute("class");
        var activeClass = liSetter.getAttribute("class");

        assertThat(openedClass, Matchers.containsString("opened"));
        assertThat(activeClass, Matchers.containsString("active"));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void check_if_sideBar_item_active_users() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost:8080/usuarios");

        HtmlListItem lisUsers = page.getFirstByXPath("//li[@id='users']");
        HtmlListItem liAll = page.getFirstByXPath("//li[@id='users_all']");

        var openedClass = lisUsers.getAttribute("class");
        var activeClass = liAll.getAttribute("class");

        assertThat(openedClass, Matchers.containsString("opened"));
        assertThat(activeClass, Matchers.containsString("active"));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void check_if_sideBar_item_active_new_user() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost:8080/usuarios/novo");

        HtmlListItem lisUsers = page.getFirstByXPath("//li[@id='users']");
        HtmlListItem liAll = page.getFirstByXPath("//li[@id='users_new']");

        var openedClass = lisUsers.getAttribute("class");
        var activeClass = liAll.getAttribute("class");

        assertThat(openedClass, Matchers.containsString("opened"));
        assertThat(activeClass, Matchers.containsString("active"));
    }

}
