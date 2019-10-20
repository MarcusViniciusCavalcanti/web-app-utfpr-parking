package br.edu.utfpr.tsi.utfparking.models.entities;

import lombok.Data;

import javax.persistence.*;

@Data
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

    public ApplicationConfig(String modeSystem) {
        this.modeSystem = modeSystem;
    }
}
