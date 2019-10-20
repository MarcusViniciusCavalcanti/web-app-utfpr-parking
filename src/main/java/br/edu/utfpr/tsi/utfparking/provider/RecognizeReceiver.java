package br.edu.utfpr.tsi.utfparking.provider;

import br.edu.utfpr.tsi.utfparking.models.dtos.PlateRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.ResultRecognizerDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.Coordinate;
import br.edu.utfpr.tsi.utfparking.models.entities.Recognize;
import br.edu.utfpr.tsi.utfparking.provider.executor.ExecutorResult;
import br.edu.utfpr.tsi.utfparking.service.CarService;
import br.edu.utfpr.tsi.utfparking.service.RecognizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecognizeReceiver {

    private final RecognizeService recognizeService;

    private final CarService carService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public void receive(PlateRecognizerDTO dto) {
        dto.getResults().stream()
                .findFirst()
                .map(PlateRecognizerDTO.Result::getPlate)
                .ifPresent(plate -> {
                    if (!recognizeService.isVerifier(plate)) {
                        var recognizes = createRecognizers(dto);

                        var coordinates = recognizes.stream()
                                .flatMap(recognize -> recognize.getCoordinates().stream())
                                .collect(Collectors.toList());

                        recognizeService.saveCoordinates(coordinates);
                        recognizeService.saveAll(recognizes);

                        var firstTwoPlace = dto.getResults().stream()
                                .sorted(Comparator.comparing(PlateRecognizerDTO.Result::getConfidence).reversed())
                                .collect(Collectors.toList());

                        var plates = firstTwoPlace.stream()
                                .map(PlateRecognizerDTO.Result::getPlate)
                                .map(String::toLowerCase)
                                .collect(Collectors.toList());

                        dto.getResults().stream()
                                .findFirst()
                                .ifPresent(result -> {
                                    var cars = carService.getCarByPlates(plates).stream()
                                            .map(car -> new ResultRecognizerDTO(car, result.getConfidence()))
                                            .collect(Collectors.toList());

                                    new ExecutorResult(simpMessagingTemplate).sendingResult(
                                            cars.isEmpty() ? List.of(new ResultRecognizerDTO(null, result.getConfidence())) : cars
                                    );
                                });
                    }
                });


    }

    private List<Recognize> createRecognizers(PlateRecognizerDTO dto) {
        var cameraId = dto.getCameraId();
        var siteId = dto.getSiteId();
        var processingTimeMs = dto.getProcessingTimeMs();
        var epochTime = dto.getEpochTime();
        var uuid = dto.getUuid();

        return dto.getResults().stream()
                .map(result -> {
                    var plate = result.getPlate();
                    var confidence = result.getConfidence();
                    var matchesTemplate = result.getMatchesTemplate();
                    var coordinates = createCoordinates(result);

                    return Recognize.builder()
                            .cameraId(cameraId)
                            .coordinates(coordinates)
                            .epochTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTime), TimeZone.getDefault().toZoneId()))
                            .origin(siteId)
                            .plate(plate)
                            .uuid(UUID.nameUUIDFromBytes((System.currentTimeMillis() + " "+ uuid).getBytes()))
                            .processingTimeMs(processingTimeMs)
                            .confidence(confidence)
                            .matchesTemplate(matchesTemplate)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<Coordinate> createCoordinates(PlateRecognizerDTO.Result result) {
        return result.getCoordinates().stream()
                .map(coordinate -> Coordinate.builder()
                        .axiosX(coordinate.getX())
                        .axiosY(coordinate.getY())
                        .build())
                .collect(Collectors.toList());
    }
}
