package it.unical.fleetgo.backend.Persistence.Entity.EmbeddableKeys;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RichiestaAffiliazioneFK {
    private int idUtente;
    private int idAzienda;
}
