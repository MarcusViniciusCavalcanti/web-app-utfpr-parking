package br.edu.utfpr.tsi.utfparking.models.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "recognizers")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter(value = AccessLevel.PACKAGE)
@Builder
public class Recognize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid_daemon")
    private UUID uuid;

    @Column(name = "camera_id")
    private Integer cameraId;

    @Column(name = "origin")
    private String origin;

    @Column(name = "epoch_time")
    private LocalDateTime epochTime;

    @Column(name = "processing_time_ms")
    private Float processingTimeMs;

    @Column(name = "plate", nullable = false, length = 10)
    private String plate;

    @Column(name = "matches_template")
    private Integer matchesTemplate;

    @Column(name = "confidence")
    private Float confidence;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "recognizes_has_coordinates",
            joinColumns = { @JoinColumn(name = "recognize_id") },
            inverseJoinColumns = { @JoinColumn(name = "coordinate_id") }
    )
    private List<Coordinate> coordinates;
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Override
    public String toString() {
        return "Recognize{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", cameraId=" + cameraId +
                ", origin='" + origin + '\'' +
                ", epochTime=" + epochTime +
                ", processingTimeMs=" + processingTimeMs +
                ", plate='" + plate + '\'' +
                ", coordinates=" + coordinates +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recognize recognize = (Recognize) o;
        return id.equals(recognize.id) &&
                uuid.equals(recognize.uuid) &&
                Objects.equals(cameraId, recognize.cameraId) &&
                origin.equals(recognize.origin) &&
                epochTime.equals(recognize.epochTime) &&
                processingTimeMs.equals(recognize.processingTimeMs) &&
                plate.equals(recognize.plate) &&
                coordinates.equals(recognize.coordinates) &&
                createdAt.equals(recognize.createdAt) &&
                updatedAt.equals(recognize.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, cameraId, origin, epochTime, processingTimeMs, plate, coordinates, createdAt, updatedAt);
    }

    @PrePersist
    private void newRecognize() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    private void updateRecognize() {
        this.updatedAt = LocalDate.now();
    }
}
