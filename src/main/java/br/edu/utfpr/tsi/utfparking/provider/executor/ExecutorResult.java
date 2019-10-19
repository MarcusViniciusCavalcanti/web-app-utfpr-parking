package br.edu.utfpr.tsi.utfparking.provider.executor;

import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class ExecutorResult {

    private ResultHandler resultHandler;

    public ExecutorResult(SimpMessagingTemplate simpMessagingTemplate) {
        initialHandlerResult(simpMessagingTemplate);
    }

    public void sendingResult(List<ResultRecognizerDTO> cars) {
        resultHandler.handleResult(cars);
    }

    private void initialHandlerResult(SimpMessagingTemplate simpMessagingTemplate) {
        var noResult = new NoResult(null, simpMessagingTemplate);
        var multipleResult = new MultipleResult(noResult, simpMessagingTemplate);
        resultHandler = new OneResult(multipleResult, simpMessagingTemplate);
    }
}
