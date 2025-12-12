package it.unical.fleetgo.backend.Models.DTO.Utente;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Utente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UtenteDTO {
    Integer idUtente;
    String nomeUtente;
    String cognomeUtente;
    String dataNascitaUtente;
    String tipoUtente;
    private String email;
    private String password;

    public UtenteDTO(Utente utente) {
        this.idUtente = utente.getIdUtente();
        this.nomeUtente = utente.getNomeUtente();
        this.cognomeUtente = utente.getCognomeUtente();
        this.dataNascitaUtente = utente.getDataNascitaUtente().toString();
    }
}