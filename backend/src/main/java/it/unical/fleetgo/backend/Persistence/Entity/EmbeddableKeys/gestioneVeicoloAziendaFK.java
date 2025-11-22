package it.unical.fleetgo.backend.Persistence.Entity.EmbeddableKeys;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class gestioneVeicoloAziendaFK {
    private Integer idVeicolo;
    private Integer idAzienda;
}
