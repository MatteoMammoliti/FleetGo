package it.unical.fleetgo.backend.Models.DTO.Utente;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class DipendenteDTO extends UtenteDTO {
    private Integer idAziendaAffiliata;
    private Set<RichiestaNoleggioDTO> richiesteNoleggio;
    private String email;
    private String urlImmagine;
    private boolean patenteAccettata;
}
