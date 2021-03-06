package br.edu.utfpr.tsi.utfparking.models.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class User implements Serializable {

    @Id
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private AccessCard accessCard;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Car car;

    @Column(name = "type")
    private String type;

    @Column(name = "authorised_access")
    private boolean authorisedAccess;

    @Column(name = "number_access")
    private Integer numberAccess;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public Optional<Car> car() {
        return Optional.ofNullable(this.car);
    }

    @PrePersist
    private void newUser() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    private void updateUser() {
        this.updatedAt = LocalDate.now();
    }

}
