package it.unical.fleetgo.backend.Models.DTO.Utente;
import it.unical.fleetgo.backend.Models.DTO.*;
import java.util.Set;

public class AdminAziendaleDTO extends UtenteDTO {
    private Integer idAziendaGestita;
    private Set<RichiestaNoleggioDTO> richiesteNoleggio;
    private Set<RichiestaManutenzioneDTO> richiesteManutenzione;
    private Set<DipendenteDTO> dipendenti;
    private Set<VeicoloDTO> veicoloInGestione;
    private Set<FatturaDTO> fatture;
    private Set<LuogoDTO> luoghiDepositoRitiro;

}
