package it.unical.fleetgo.backend.Persistence.Entity;
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
    private Integer idAzienda;
    private LocalDate dataRitiro;
    private LocalDate dataConsegna;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String motivazione;
    private Boolean richiestaAccettata;


}
