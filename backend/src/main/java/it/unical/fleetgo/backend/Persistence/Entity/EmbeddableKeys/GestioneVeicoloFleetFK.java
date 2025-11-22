package it.unical.fleetgo.backend.Persistence.Entity.EmbeddableKeys;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class GestioneVeicoloFleetFK {
    private Integer idFleet;
    private Integer idVeicolo;
}
