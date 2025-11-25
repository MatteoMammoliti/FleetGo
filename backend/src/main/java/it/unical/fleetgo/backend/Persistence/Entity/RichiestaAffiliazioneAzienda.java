package it.unical.fleetgo.backend.Persistence.Entity;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Utente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RichiestaAffiliazioneAzienda {
    private Utente utente;
    private Integer idUtente;
    private Integer idAzienda;
    private boolean accettata;
    private LocalDate dataAccettazione;
}
