package br.edu.utfpr.tsi.utfparking.models.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "application_config")
@EqualsAndHashCode
@ToString
public class ApplicationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mode_system")
    private String modeSystem;

    @Column(name = "ip")
    private String ip;

    public ApplicationConfig(long id, String modeSystem) {
        this.id = id;
        this.modeSystem = modeSystem;
    }
}
