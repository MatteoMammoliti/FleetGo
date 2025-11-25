package it.unical.fleetgo.backend.Models.DTO.Utente;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;

import java.util.Set;

public class FleetGoDTO extends UtenteDTO {
    private Set<RichiestaManutenzioneDTO> richiesteManutenzione;
    private Set<VeicoloDTO> veicoloDisponibili;
    private Set<AziendaDTO> aziende;
    private Set<FatturaDTO> fattureEmesse;
}
