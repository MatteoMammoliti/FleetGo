package it.unical.fleetgo.backend.Persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FleetGo extends Utente{
    private Set<RichiestaManutenzione> richiesteManutenzione;
    private Set<Veicolo> veicoloDisponibili;
    private Set<Azienda> aziende;
    private Set<Fattura> fattureEmesse;

}
