package it.unical.fleetgo.backend.Persistence.Entity.Utente;

import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Dipendente extends Utente {
    private Integer idAziendaAffiliata;
    private List<RichiestaNoleggio> richiesteNoleggio;
    private CredenzialiUtente credenziali;
}
