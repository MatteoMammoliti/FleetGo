package it.unical.fleetgo.backend.Models.DTO.Utente;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class DipendenteDTO extends UtenteDTO {
    private Integer idAziendaAffiliata;
    private List<RichiestaNoleggioDTO> richiesteNoleggio;
    private String urlImmagine;
    private boolean patenteAccettata;
}
