package br.edu.utfpr.tsi.utfparking.provider.executor;

import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import br.edu.utfpr.tsi.utfparking.utils.ToolsTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
public class MultipleResultTest {

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    public void should_sending_message_of_no_result_when_multiple_result() {
        var multipleResult = new MultipleResult(null, simpMessagingTemplate);

        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver.setId(1L);
        var car = Car.builder()
                .id(1L)
                .user(driver)
                .plate("ABC1234")
                .model("model")
                .lastAccess(LocalDateTime.now())
                .build();

        multipleResult.handleResult(List.of(new ResultRecognizerDTO(car, 10.0F), new ResultRecognizerDTO(car, 10.0F)));

        Mockito.verify(simpMessagingTemplate, Mockito.times(1)).convertAndSend(Mockito.anyString(), Mockito.any(RecognizerDTO.class));
    }

    @Test
    public void should_sending_message_with_last_access_is_major_10_minutes() {
        var multipleResult = new MultipleResult(null, simpMessagingTemplate);
        var driver1 = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver1.setId(1L);
        var carLastAccess = Car.builder()
                .id(1L)
                .user(driver1)
                .plate("ABC1234")
                .model("model")
                .lastAccess(LocalDateTime.now().minusMinutes(15))
                .build();

        var driver2 = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver2.setId(2L);
        var carWithoutAccess = Car.builder()
                .id(2L)
                .user(driver2)
                .plate("EFJ5678")
                .model("model")
                .lastAccess(LocalDateTime.now())
                .build();

        multipleResult.handleResult(List.of(new ResultRecognizerDTO(carLastAccess, 10.0F), new ResultRecognizerDTO(carWithoutAccess, 10.1F)));

        var argumentCaptor = ArgumentCaptor.forClass(RecognizerDTO.class);
        Mockito.verify(simpMessagingTemplate).convertAndSend(Mockito.anyString(), argumentCaptor.capture());
        Mockito.verify(simpMessagingTemplate, Mockito.times(1)).convertAndSend(Mockito.anyString(), Mockito.any(RecognizerDTO.class));

        assertThat(argumentCaptor.getValue().getDriver().getUserId(), Matchers.is(2L));
        assertThat(argumentCaptor.getValue().getTax(), Matchers.is(10.1F));
        assertThat(argumentCaptor.getValue().getDriver().getPlate(), Matchers.is("EFJ5678"));
    }

    @Test
    public void should_sending_message_with_major_confidence_when_last_access_coincide() {
        var now = LocalDateTime.now();

        var multipleResult = new MultipleResult(null, simpMessagingTemplate);
        var driver1 = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver1.setId(1L);
        var carMinorConfidence = Car.builder()
                .id(1L)
                .user(driver1)
                .plate("ABC1234")
                .model("model")
                .lastAccess(now)
                .build();

        var driver2 = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver2.setId(2L);
        var carMajorConfidence = Car.builder()
                .id(2L)
                .user(driver2)
                .plate("EFJ5678")
                .model("model")
                .lastAccess(now)
                .build();

        multipleResult.handleResult(List.of(new ResultRecognizerDTO(carMinorConfidence, 10.0F), new ResultRecognizerDTO(carMajorConfidence, 10.1F)));

        var argumentCaptor = ArgumentCaptor.forClass(RecognizerDTO.class);
        Mockito.verify(simpMessagingTemplate).convertAndSend(Mockito.anyString(), argumentCaptor.capture());
        Mockito.verify(simpMessagingTemplate, Mockito.times(1)).convertAndSend(Mockito.anyString(), Mockito.any(RecognizerDTO.class));

        assertThat(argumentCaptor.getValue().getDriver().getUserId(), Matchers.is(2L));
        assertThat(argumentCaptor.getValue().getTax(), Matchers.is(10.1F));
        assertThat(argumentCaptor.getValue().getDriver().getPlate(), Matchers.is("EFJ5678"));
    }

    @Test
    public void should_pass_next_executor_sending_message_when_one_result() {
        var multipleResult = new MultipleResult(null, simpMessagingTemplate);

        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver.setId(1L);
        var car = Car.builder()
                .id(1L)
                .user(driver)
                .plate("ABC1234")
                .model("model")
                .lastAccess(LocalDateTime.now())
                .build();

        multipleResult.handleResult(List.of(new ResultRecognizerDTO(car, 10.0F)));

        Mockito.verify(simpMessagingTemplate, Mockito.times(0)).convertAndSend(Mockito.anyString(), Mockito.any(RecognizerDTO.class));
    }

    @Test
    public void should_pass_next_executor_sending_message_when_no_result() {
        var multipleResult = new MultipleResult(null, simpMessagingTemplate);

        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver.setId(1L);
        multipleResult.handleResult(List.of(new ResultRecognizerDTO(null, 10.0F)));

        Mockito.verify(simpMessagingTemplate, Mockito.times(0)).convertAndSend(Mockito.anyString(), Mockito.any(RecognizerDTO.class));
    }

}
