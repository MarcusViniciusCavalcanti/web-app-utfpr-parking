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

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", plate='" + plate + '\'' +
                ", model='" + model + '\'' +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id.equals(car.id) &&
                plate.equals(car.plate) &&
                model.equals(car.model) &&
                user.equals(car.user) &&
                Objects.equals(createdAt, car.createdAt) &&
                Objects.equals(updatedAt, car.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate, model, user, createdAt, updatedAt);
    }

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
