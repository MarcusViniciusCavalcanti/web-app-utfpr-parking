package br.edu.utfpr.tsi.utfparking.provider;

import br.edu.utfpr.tsi.utfparking.data.RecognizeRepository;
import br.edu.utfpr.tsi.utfparking.models.dtos.PlateRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Recognize;
import br.edu.utfpr.tsi.utfparking.service.CarService;
import br.edu.utfpr.tsi.utfparking.service.RecognizeService;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecognizeReceiverTest {

    @Autowired
    private RecognizeRepository recognizeRepository;

    @Autowired
    private RecognizeService recognizeService;

    @Autowired
    private CarService carService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @After
    public void resetDatabase() {
        recognizeRepository.deleteAll();;
    }

    @Test
    public void when_receive_dto_sanding_message() {
        var plateRecognizerDTO = new PlateRecognizerDTO();

        plateRecognizerDTO.setCameraId(1);
        plateRecognizerDTO.setEpochTime(System.currentTimeMillis());
        plateRecognizerDTO.setImgHeight(480);
        plateRecognizerDTO.setImgWidth(640);
        plateRecognizerDTO.setProcessingTimeMs(138.669163f);

        var result = new PlateRecognizerDTO.Result();
        result.setConfidence(89.130661f);
        result.setMatchesTemplate(0);
        result.setRegion("");
        result.setPlate("AAY5127");

        var coordinate = new PlateRecognizerDTO.Coordinate();
        coordinate.setX(218f);
        coordinate.setY(218f);

        result.setCoordinates(List.of(coordinate));
        plateRecognizerDTO.setResults(List.of(result));

        var recognizeReceiver = new RecognizeReceiver(recognizeService, carService, simpMessagingTemplate);
        recognizeReceiver.receive(plateRecognizerDTO);

        Mockito.verify(simpMessagingTemplate, Mockito.times(1)).convertAndSend(Mockito.anyString(), Mockito.any(Object.class));

        var statement = recognizeRepository.findAll().stream()
                .map(Recognize::getPlate)
                .collect(Collectors.toList());

        assertThat(statement, Matchers.hasItems("AAY5127"));
    }

}
