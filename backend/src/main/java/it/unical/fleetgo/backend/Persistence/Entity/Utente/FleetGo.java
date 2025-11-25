package it.unical.fleetgo.backend.Persistence.Entity.Utente;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import it.unical.fleetgo.backend.Persistence.Entity.Fattura;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaManutenzione;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FleetGo extends Utente {
    private Set<RichiestaManutenzione> richiesteManutenzione;
    private Set<Veicolo> veicoloDisponibili;
    private Set<Azienda> aziende;
    private Set<Fattura> fattureEmesse;
}
