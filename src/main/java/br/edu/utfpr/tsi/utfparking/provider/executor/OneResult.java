package br.edu.utfpr.tsi.utfparking.provider.executor;

import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class OneResult extends ResultHandler {

    public OneResult(ResultHandler next, SimpMessagingTemplate simpMessagingTemplate) {
        super(next, simpMessagingTemplate);
    }

    @Override
    public void handleResult(List<ResultRecognizerDTO> results) {
        if (results.size() == 1 && results.get(0).getCar() != null) {
            var car = results.get(0).getCar();
            var confidence = results.get(0).getConfidence();
            var dto = RecognizerDTO.getNewInstance(car, confidence);
            super.sending(dto);
        }

        super.handleResult(results);
    }
}
