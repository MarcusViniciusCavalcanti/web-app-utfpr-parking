package br.edu.utfpr.tsi.utfparking.models.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cars", indexes = { @Index(columnList = "plate", name = "Index_car_plate") })
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@Setter(value = AccessLevel.PACKAGE)
@Builder
public class Car {

    @Id
    private Long id;

    @Column(name = "plate", nullable = false, length = 10)
    private String plate;

    @Column(name = "model", nullable = false)
    private String model;

    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "last_access")
    private LocalDateTime lastAccess;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    private void newCar() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    private void updateCar() {
        this.updatedAt = LocalDate.now();
    }
}
