package br.edu.utfpr.tsi.utfparking.controller;

import br.edu.utfpr.tsi.utfparking.UtfparkingApplication;
import br.edu.utfpr.tsi.utfparking.data.RecognizeRepository;
import br.edu.utfpr.tsi.utfparking.filters.CheckRequestDevice;
import br.edu.utfpr.tsi.utfparking.models.entities.ApplicationConfig;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.util.Objects.requireNonNull;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = UtfparkingApplication.class)
@ActiveProfiles("test")
public class RecognizerPlateControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CheckRequestDevice checkRequestDevice;

    @Autowired
    private RecognizeRepository recognizeRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(checkRequestDevice)
                .build();
    }

    @Test
    public void should_return_access_denied_when_ifs_not_allow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/send/plate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.is("access denied")));
    }

    @Test
    public void should_pass_when_ip_is_allow() throws Exception {
        var config = (ApplicationConfig) requireNonNull(webApplicationContext.getServletContext()).getAttribute("config");
        config.setIp("0.0.0.0");

        webApplicationContext.getServletContext().setAttribute("config", config);

        mockMvc.perform(MockMvcRequestBuilders.post("/send/plate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"uuid\": \"f11e0acc-6aaf-4817-9299-9e6773043b8e\",\n" +
                        "  \"camera_id\": 1,\n" +
                        "  \"site_id\": \"headquarters\",\n" +
                        "  \"img_width\": 640,\n" +
                        "  \"img_height\": 480,\n" +
                        "  \"epoch_time\": 1402161050,\n" +
                        "  \"processing_time_ms\": 138.669163,\n" +
                        "  \"results\": [\n" +
                        "    {\n" +
                        "      \"plate\": \"AAY5127\",\n" +
                        "      \"confidence\": 89.130661,\n" +
                        "      \"matches_template\": 0,\n" +
                        "      \"region\": \"\",\n" +
                        "      \"region_confidence\": 0,\n" +
                        "      \"coordinates\": [\n" +
                        "        {\n" +
                        "          \"x\": 218,\n" +
                        "          \"y\": 342\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"x\": 407,\n" +
                        "          \"y\": 325\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"x\": 407,\n" +
                        "          \"y\": 413\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"x\": 218,\n" +
                        "          \"y\": 431\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"candidates\": [\n" +
                        "        {\n" +
                        "          \"plate\": \"S11FRE\",\n" +
                        "          \"confidence\": 77.130661,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11ERE\",\n" +
                        "          \"confidence\": 75.496307,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11RE\",\n" +
                        "          \"confidence\": 75.440361,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11CRE\",\n" +
                        "          \"confidence\": 75.340179,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11FHE\",\n" +
                        "          \"confidence\": 75.240974,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11EHE\",\n" +
                        "          \"confidence\": 73.606621,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11HE\",\n" +
                        "          \"confidence\": 73.550682,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11CHE\",\n" +
                        "          \"confidence\": 73.450493,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11FBE\",\n" +
                        "          \"confidence\": 71.782944,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"S11FE\",\n" +
                        "          \"confidence\": 71.762756,\n" +
                        "          \"matches_template\": 0\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"plate\": \"EJLESSIE\",\n" +
                        "      \"epoch_time\": 1402158050,\n" +
                        "      \"confidence\": 78.167984,\n" +
                        "      \"matches_template\": 0,\n" +
                        "      \"region\": \"\",\n" +
                        "      \"region_confidence\": 0,\n" +
                        "      \"processing_time_ms\": 51.650604,\n" +
                        "      \"coordinates\": [\n" +
                        "        {\n" +
                        "          \"x\": 226,\n" +
                        "          \"y\": 369\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"x\": 348,\n" +
                        "          \"y\": 348\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"x\": 355,\n" +
                        "          \"y\": 406\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"x\": 231,\n" +
                        "          \"y\": 429\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"candidates\": [\n" +
                        "        {\n" +
                        "          \"plate\": \"EJLESSIE\",\n" +
                        "          \"confidence\": 78.167984,\n" +
                        "          \"matches_template\": 0\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"plate\": \"EDLESSIE\",\n" +
                        "          \"confidence\": 77.61319,\n" +
                        "          \"matches_template\": 0\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .with(mockHttpServletRequest -> {
                    mockHttpServletRequest.setRemoteAddr("0.0.0.0");
                    return mockHttpServletRequest;
                })).andExpect(MockMvcResultMatchers.status().isOk());

        recognizeRepository.findAll().stream()
                .filter(recognize -> recognize.getPlate().equals("AAY5127"))
                .findFirst()
                .orElseThrow();
    }
}
