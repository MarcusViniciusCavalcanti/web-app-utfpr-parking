package br.edu.utfpr.tsi.utfparking.provider.executor;

import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import br.edu.utfpr.tsi.utfparking.utils.ToolsTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NoResultTest {

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    public void should_sending_message_of_no_result_when_no_result() {
        var noResult = new NoResult(null, simpMessagingTemplate);
        noResult.handleResult(List.of(new ResultRecognizerDTO(null, 10.0F)));

        Mockito.verify(simpMessagingTemplate, Mockito.times(1)).convertAndSend(Mockito.anyString(), Mockito.any(RecognizerDTO.class));
    }


    @Test
    public void should_pass_next_executor_sending_message_when_one_result() {
        var oneResult = new NoResult(null, simpMessagingTemplate);

        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver.setId(1L);
        var car = Car.builder()
                .id(1L)
                .user(driver)
                .plate("ABC1234")
                .model("model")
                .lastAccess(LocalDateTime.now())
                .build();

        oneResult.handleResult(List.of(new ResultRecognizerDTO(car, 10.0F)));

        Mockito.verify(simpMessagingTemplate, Mockito.times(0)).convertAndSend(Mockito.anyString(), Mockito.any(RecognizerDTO.class));
    }

    @Test
    public void should_pass_next_executor_message_when_more_result() {
        var oneResult = new NoResult(null, simpMessagingTemplate);

        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver.setId(1L);
        var car = Car.builder()
                .id(1L)
                .user(driver)
                .plate("ABC1234")
                .model("model")
                .lastAccess(LocalDateTime.now())
                .build();

        oneResult.handleResult(List.of(new ResultRecognizerDTO(car, 10.0F), new ResultRecognizerDTO(car, 10.0F)));

        Mockito.verify(simpMessagingTemplate, Mockito.times(0)).convertAndSend(Mockito.anyString(), Mockito.any(RecognizerDTO.class));
    }
}
