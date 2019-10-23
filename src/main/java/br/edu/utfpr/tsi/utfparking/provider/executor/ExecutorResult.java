package br.edu.utfpr.tsi.utfparking.provider.executor;

import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class ExecutorResult {

    private ResultHandler resultHandler;

    public ExecutorResult(SimpMessagingTemplate simpMessagingTemplate) {
        var noResult = new NoResult(null, simpMessagingTemplate);
        var multipleResult = new MultipleResult(noResult, simpMessagingTemplate);
        var oneResult = new OneResult(multipleResult, simpMessagingTemplate);

        initialHandlerResult(oneResult);
    }

    public void sendingResult(List<ResultRecognizerDTO> cars) {
        resultHandler.handleResult(cars);
    }

    protected void initialHandlerResult(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }
}
