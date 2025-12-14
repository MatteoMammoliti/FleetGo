package it.unical.fleetgo.backend.Persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Offerta {

    private Integer idOfferta;
    private String nomeOfferta;
    private String descrizioneOfferta;
    private LocalDate scadenza;
    private Integer percentualeSconto;
    private String immagineCopertina;
}