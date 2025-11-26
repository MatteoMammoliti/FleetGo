package it.unical.fleetgo.backend.Persistence.Entity.Utente;
import it.unical.fleetgo.backend.Persistence.Entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class AdminAziendale extends Utente {
    private Integer idAziendaGestita;
    private Set<RichiestaNoleggio> richiesteNoleggio;
    private Set<RichiestaManutenzione> richiesteManutenzione;
    private List<RichiestaAffiliazioneAzienda> richiesteAffiliazione;
    private List<Dipendente> dipendenti;
    private Set<Veicolo> veicoliInGestione;
    private Set<Fattura> fatture;
    private Set<LuogoAzienda> luoghiDepositoRitiro;
}
