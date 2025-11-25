package it.unical.fleetgo.backend.Persistence.Entity;


import it.unical.fleetgo.backend.Models.ContenitoreCredenziali;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Dipendente extends Utente{
    private Integer idAziendaAffiliata;
    private Set<RichiestaNoleggio> richiesteNoleggio;
    private ContenitoreCredenziali credenziali;
}
