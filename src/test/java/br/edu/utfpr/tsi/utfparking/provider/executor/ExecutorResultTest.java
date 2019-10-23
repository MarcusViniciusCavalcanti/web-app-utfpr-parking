package br.edu.utfpr.tsi.utfparking.provider.executor;

import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import br.edu.utfpr.tsi.utfparking.utils.ToolsTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
public class ExecutorResultTest {

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private NoResult noResult;

    @MockBean
    private OneResult oneResult;

    @MockBean
    private MultipleResult multipleResult;

    @Test
    public void should_have_call_handler() {
        var executorResult = new ExecutorResult(simpMessagingTemplate);

        Mockito.when(noResult.getNext()).thenReturn(null);
        Mockito.when(multipleResult.getNext()).thenReturn(noResult);
        Mockito.when(oneResult.getNext()).thenReturn(multipleResult);

        executorResult.initialHandlerResult(oneResult);

        var driver1 = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver1.setId(1L);
        var car1 = Car.builder()
                .id(1L)
                .user(driver1)
                .plate("ABC1234")
                .model("model")
                .lastAccess(LocalDateTime.now().minusMinutes(15))
                .build();

        var driver2 = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver2.setId(2L);
        var car2 = Car.builder()
                .id(2L)
                .user(driver2)
                .plate("EFJ5678")
                .model("model")
                .lastAccess(LocalDateTime.now())
                .build();


        var cars = List.of(new ResultRecognizerDTO(car1, 10.0F), new ResultRecognizerDTO(car2, 10.1F));

        executorResult.sendingResult(cars);

        Mockito.doCallRealMethod().when(oneResult).handleResult(cars);
        oneResult.handleResult(cars);

        Mockito.doCallRealMethod().when(noResult).handleResult(cars);
        noResult.handleResult(cars);

        var dto = RecognizerDTO.getNewInstance(cars.get(0).getCar(), cars.get(0).getConfidence());
        Mockito.doNothing().when(multipleResult).sending(dto);

        Mockito.verify(multipleResult, Mockito.times(1)).handleResult(Mockito.any());
        Mockito.verify(noResult, Mockito.times(1)).handleResult(Mockito.anyList());
        Mockito.verify(oneResult, Mockito.times(2)).handleResult(Mockito.anyList());
    }
}
