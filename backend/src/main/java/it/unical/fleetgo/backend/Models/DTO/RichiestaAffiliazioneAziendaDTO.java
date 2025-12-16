package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RichiestaAffiliazioneAziendaDTO {
    private UtenteDTO utente;
    private Integer idUtente;
    private Integer idAzienda;
    private boolean accettata;
    private String dataAccettazione;
}