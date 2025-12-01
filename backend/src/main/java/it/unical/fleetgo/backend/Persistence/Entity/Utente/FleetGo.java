package it.unical.fleetgo.backend.Persistence.Entity.Utente;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import it.unical.fleetgo.backend.Persistence.Entity.Fattura;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaManutenzione;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FleetGo extends Utente {
    private List<RichiestaManutenzione> richiesteManutenzione;
    private List<Veicolo> veicoloDisponibili;
    private List<Azienda> aziende;
    private List<Fattura> fattureEmesse;
}