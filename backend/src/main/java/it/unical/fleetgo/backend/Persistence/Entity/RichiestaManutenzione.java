package it.unical.fleetgo.backend.Persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class RichiestaManutenzione {
    private Integer idManutenzione;
    private Integer idAdmin;
    private Integer idVeicolo;
    private Veicolo veicolo;
    private LocalDate dataRichiesta;
    private String tipoManutenzione;
    private Boolean richiestaAccettata;
    private Boolean richiestaCompletata;
}
