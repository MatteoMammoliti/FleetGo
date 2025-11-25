package it.unical.fleetgo.backend.Persistence.Entity.Utente;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public abstract class Utente {
    private Integer idUtente;
    private String nomeUtente;
    private String cognomeUtente;
    private LocalDate dataNascitaUtente;
}
