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
        if (this.getNext() != null) {
            this.getNext().handleResult(results);
        }
    }

    public ResultHandler getNext() {
        return this.next;
    }

    protected void sending(RecognizerDTO message) {
        log.info("{} handling result '{}'", this, message);
        simpMessagingTemplate.convertAndSend("/topic/recognize", message);
    }

}
