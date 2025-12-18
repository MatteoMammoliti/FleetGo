package it.unical.fleetgo.backend.Models.DTO.Utente;
import it.unical.fleetgo.backend.Models.DTO.*;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.AdminAziendale;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Utente;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminAziendaleDTO extends UtenteDTO {
    private Integer idAziendaGestita;
    private AziendaDTO aziendaGestita;

    public AdminAziendaleDTO() {
        super();
    }

    public AdminAziendaleDTO(AdminAziendale utente, boolean caricaInformazioniAzienda) {
        super(utente);

        if(caricaInformazioniAzienda){
            this.idAziendaGestita = utente.getIdAziendaGestita();
            this.aziendaGestita = new AziendaDTO(utente.getAziendaGestita());
        } else {
            this.idAziendaGestita = null;
            this.aziendaGestita = null;
        }
    }
}