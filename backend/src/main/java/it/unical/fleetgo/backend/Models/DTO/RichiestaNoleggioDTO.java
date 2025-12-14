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
    private VeicoloDTO veicolo;
    private String oraInizio;
    private String oraFine;
    private String dataRitiro;
    private String dataConsegna;
    private String motivazione;
    private Boolean accettata;
    private UtenteDTO utente;
    private Boolean richiestaAnnullata;
    private Float costoNoleggio;
    private String statoRichiesta;

    public RichiestaNoleggioDTO() {}
    public RichiestaNoleggioDTO(RichiestaNoleggio richiesta, boolean caricaUtente) {
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

        if(caricaUtente) this.utente = new DipendenteDTO((Dipendente) richiesta.getUtente());
        else this.utente = null;
    }
}
