package it.unical.fleetgo.backend.Models.DTO.Utente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class UtenteDTO {
    Integer idUtente;
    String nomeUtente;
    String cognomeUtente;
    LocalDate dataNascitaUtente;
    String tipoUtente;
}
