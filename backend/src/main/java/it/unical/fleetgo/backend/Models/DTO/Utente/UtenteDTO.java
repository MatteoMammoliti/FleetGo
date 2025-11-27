package it.unical.fleetgo.backend.Models.DTO.Utente;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class UtenteDTO {
    Integer idUtente;
    String nomeUtente;
    String cognomeUtente;
    //ANNO-MESE-GIORNO
    String dataNascitaUtente;
    String tipoUtente;
    private String email;
    private String password;
}
