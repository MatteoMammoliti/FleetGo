package it.unical.fleetgo.backend.Persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class RichiestaNoleggio {
    private Integer idRichiestaNoleggio;
    private Utente utente;
    private Azienda azienda;
    private LocalDate dataRitiro;
    private LocalDate dataConsegna;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String motivazione;
    private Boolean richiestaAccettata;


}
