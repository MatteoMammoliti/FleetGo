package it.unical.fleetgo.backend.Models.DTO.Utente;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Utente;
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

    public DipendenteDTO() {}

    public DipendenteDTO(Dipendente utente) {
        super(utente);
        this.idAziendaAffiliata = utente.getIdAziendaAffiliata();
        this.urlImmagine = utente.getCredenziali().getImgPatente();
        this.patenteAccettata = utente.getCredenziali().isPatente();
        this.setEmail(utente.getCredenziali().getEmail());
    }
}