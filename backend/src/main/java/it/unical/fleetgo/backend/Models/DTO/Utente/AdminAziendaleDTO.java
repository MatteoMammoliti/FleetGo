package it.unical.fleetgo.backend.Models.DTO.Utente;
import it.unical.fleetgo.backend.Models.DTO.*;
import java.util.List;

public class AdminAziendaleDTO extends UtenteDTO {
    private Integer idAziendaGestita;
    private List<RichiestaNoleggioDTO> richiesteNoleggio;
    private List<RichiestaManutenzioneDTO> richiesteManutenzione;
    private List<DipendenteDTO> dipendenti;
    private List<GestioneVeicoloAziendaDTO> veicoloInGestione;
    private List<FatturaDTO> fatture;
    private List<LuogoDTO> luoghiDepositoRitiro;
}