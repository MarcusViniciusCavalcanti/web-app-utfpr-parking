package br.edu.utfpr.tsi.utfparking.web.controller;

import br.edu.utfpr.tsi.utfparking.models.dtos.PlateRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.RecognizerDTO;
import br.edu.utfpr.tsi.utfparking.provider.RecognizeReceiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecognizerPlateController {

    private final RecognizeReceiver recognizeReceiver;

    @PostMapping("/send/plate")
    public ResponseEntity<String> identifierPlate(@RequestBody PlateRecognizerDTO dto) {
        var results = dto.getResults().stream()
                .filter(r -> r.getConfidence() > 75)
                .collect(Collectors.toList());

        if (!results.isEmpty()) {
            recognizeReceiver.receive(dto);
        }

        return ResponseEntity.ok().build();
    }

    @MessageMapping("/recognize")
    @SendTo("/history")
    public RecognizerDTO send(@Payload RecognizerDTO message) {
        return message;
    }
}
