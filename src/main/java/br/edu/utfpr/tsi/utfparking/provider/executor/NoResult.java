package br.edu.utfpr.tsi.utfparking.provider.executor;

import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class NoResult extends ResultHandler {

    public NoResult(ResultHandler next, SimpMessagingTemplate simpMessagingTemplate) {
        super(next, simpMessagingTemplate);
    }

    @Override
    public void handleResult(List<ResultRecognizerDTO> results) {
        if (!results.isEmpty() && results.get(0).getCar() == null) {
            var message = RecognizerDTO.getNewInstance(null, results.get(0).getConfidence());
            super.sending(message);
        }

        super.handleResult(results);
    }
}
