package br.edu.utfpr.tsi.utfparking.provider.executor;

import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@Slf4j
public abstract class ResultHandler {

    private ResultHandler next;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ResultHandler(ResultHandler next, SimpMessagingTemplate simpMessagingTemplate) {
        this.next = next;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void handleResult(List<ResultRecognizerDTO> results) {
        if (next != null) {
            next.handleResult(results);
        }
    }

    protected void sending(RecognizerDTO message) {
        log.info("{} handling result '{}'", this, message);
        simpMessagingTemplate.convertAndSend("/topic/recognize", message);
    }

}
