package it.unical.fleetgo.backend.Persistence.Entity.EmbeddableKeys;

import jakarta.persistence.Embeddable;
import lombok.Data;


@Embeddable
@Data
public class FatturaFk {
    private Integer annoFattura;
    private Integer meseFattura;
    private Integer idAzienda;
    private Integer idFleet;
}
