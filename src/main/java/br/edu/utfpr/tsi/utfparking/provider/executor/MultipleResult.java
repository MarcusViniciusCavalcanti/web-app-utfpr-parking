package br.edu.utfpr.tsi.utfparking.provider.executor;
;
import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
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
                        var hour = LocalDateTime.now().getHour();
                        var minute = LocalDateTime.now().getMinute();

                        var lastHourAccess = car.getCar().getLastAccess().getHour();
                        var lastMinuteAccess = car.getCar().getLastAccess().getMinute();

                        return (lastHourAccess + lastMinuteAccess) < (hour + minute);
                    })
                    .findFirst()
                    .ifPresentOrElse(result -> {
                        var dto = RecognizerDTO.getNewInstance(result.getCar(), results.get(0).getConfidence());
                        this.sending(dto);
                    }, () -> {
                        var car = results.get(0).getCar();
                        var dto = RecognizerDTO.getNewInstance(car, results.get(0).getConfidence());
                        this.sending(dto);
                    });

        }

        super.handleResult(results);
    }
}
