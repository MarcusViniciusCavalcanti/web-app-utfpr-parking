package br.edu.utfpr.tsi.utfparking.models.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(value = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Builder
@EqualsAndHashCode
@ToString
public class Role implements Serializable {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "name", nullable = false, length = 30, unique = true)
    private String name;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

}
