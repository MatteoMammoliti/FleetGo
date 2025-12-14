package it.unical.fleetgo.backend.Persistence.Entity;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Utente;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class RichiestaNoleggio {
    private Integer idRichiestaNoleggio;
    private Integer idVeicolo;
    private Integer idUtente;
    private Utente utente;
    private Veicolo veicolo;
    private Integer idAzienda;
    private String dataRitiro;
    private String dataConsegna;
    private String oraInizio;
    private String oraFine;
    private String motivazione;
    private Boolean richiestaAccettata;
    private Boolean richiestaAnnullata;
    private Float costo;
    private String statoRichiesta;
}
