package br.edu.utfpr.tsi.utfparking.models.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "application_config")
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
