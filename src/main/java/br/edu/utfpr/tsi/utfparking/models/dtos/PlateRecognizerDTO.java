package br.edu.utfpr.tsi.utfparking.models.dtos;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class )
public class PlateRecognizerDTO {

    private String uuid;
    private Integer cameraId;
    private String siteId;
    private Integer imgWidth;
    private Integer imgHeight;
    private Long epochTime;
    private Float processingTimeMs;
    private List<Result> results;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class )
    public static class Result {
        private String plate;
        private Float confidence;
        private Integer matchesTemplate;
        private String region;
        private Integer regionConfidence;
        private List<Coordinate> coordinates;
        private List<Candidate> candidates;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class )
    public static class Coordinate {
        private Float x;
        private Float y;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class )
    public static class Candidate {
        private String plate;
        private Float confidence;
        private Integer matchesTemplate;
    }
}
