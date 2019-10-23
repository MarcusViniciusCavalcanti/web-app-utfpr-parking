package br.edu.utfpr.tsi.utfparking.provider.executor;
;
import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class MultipleResult extends ResultHandler {

    public MultipleResult(ResultHandler next, SimpMessagingTemplate simpMessagingTemplate) {
        super(next, simpMessagingTemplate);
    }

    @Override
    public void handleResult(List<ResultRecognizerDTO> results) {
        if (results.size() > 1) {
            results.stream()
                    .filter(car -> {
                        var lastAccess = car.getCar().getLastAccess();
                        return LocalDateTime.now().minusMinutes(10).isBefore(lastAccess);
                    })
                    .max(Comparator.comparing(ResultRecognizerDTO::getConfidence))
                    .ifPresent(result -> {
                        var dto = RecognizerDTO.getNewInstance(result.getCar(), result.getConfidence());
                        this.sending(dto);
                    });

        }

        super.handleResult(results);
    }
}
