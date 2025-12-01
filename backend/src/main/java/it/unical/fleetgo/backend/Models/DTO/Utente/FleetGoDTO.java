package it.unical.fleetgo.backend.Models.DTO.Utente;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import java.util.List;

public class FleetGoDTO extends UtenteDTO {
    private List<RichiestaManutenzioneDTO> richiesteManutenzione;
    private List<VeicoloDTO> veicoloDisponibili;
    private List<AziendaDTO> aziende;
    private List<FatturaDTO> fattureEmesse;
}
