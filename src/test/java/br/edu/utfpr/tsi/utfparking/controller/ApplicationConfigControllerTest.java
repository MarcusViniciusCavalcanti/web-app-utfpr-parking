package br.edu.utfpr.tsi.utfparking.controller;

import com.gargoylesoftware.htmlunit.WebClient;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/sql/basic_user.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:/sql/delete_basic_user.sql"})
public class ApplicationConfigControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private WebClient webClient;

    @Before
    public void setup() {

        this.mockMvc = MockMvcBuilders
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
    public void check_if_config_is_load() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/configuracoes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("config/show"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("config"))
                .andExpect(MockMvcResultMatchers.model().attribute("config", Matchers.hasProperty("id", Matchers.notNullValue())));
    }

    @Test
    @WithUserDetails(value = "vinicius_admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void should_changed_config() throws Exception {
        var url = "http://localhost:8080/configuracoes";
        HtmlPage page = webClient.getPage(url);

        var formConfig = page.getFormByName("form-config");

        formConfig.getRadioButtonsByName("modeSystem").stream()
                .peek(radio -> radio.setChecked(false))
                .filter(radio -> radio.getValueAttribute().equals("automatic"))
                .findFirst()
                .ifPresentOrElse(radio -> {
                    radio.setChecked(true);
                    formConfig.getInputByName("ip").setValueAttribute("192.168.1.1");

                    try {
                        var submit = formConfig.getOneHtmlElementByAttribute("button", "type", "submit");
                        HtmlPage redirectPage = submit.click();
                        var result = redirectPage.asXml()
                                .contains("sistema controla a abertura do dispositivo do portão.");

                        assertThat(result, Matchers.is(true));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, () -> {
                    throw new RuntimeException("radio button of value automatic not found in html");
                });
    }
}
