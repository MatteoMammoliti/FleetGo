package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichiestaNoleggioDTO {
    private Integer idRichiesta;
    private Integer idDipendente;
    private Integer idAziendaRiferimento;
    private Integer idVeicolo;
    private String oraInizio;
    private String oraFine;
    private String dataRitiro;
    private String dataConsegna;
    private String motivazione;
    private boolean accettata;
    private UtenteDTO utente;
    private Boolean richiestaAnnullata;

    public RichiestaNoleggioDTO(RichiestaNoleggio richiesta) {
        this.idRichiesta = richiesta.getIdRichiestaNoleggio();
        this.idDipendente = richiesta.getIdUtente();
        this.idAziendaRiferimento = richiesta.getIdAzienda();
        this.idVeicolo = richiesta.getIdVeicolo();
        this.oraInizio = richiesta.getOraInizio().toString();
        this.oraFine = richiesta.getOraFine().toString();
        this.dataRitiro = richiesta.getDataRitiro().toString();
        this.dataConsegna = richiesta.getDataConsegna().toString();
        this.motivazione = richiesta.getMotivazione();
        this.accettata = richiesta.getRichiestaAccettata();
        this.richiestaAnnullata = richiesta.getRichiestaAnnullata();
        this.utente = new DipendenteDTO((Dipendente) richiesta.getUtente());
    }
}
