package it.unical.fleetgo.backend.Persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Azienda {
    private int idAzienda;
    private Utente admin;
    private String sedeAzienda;
    private String nomeAzienda;
    private String pIva;
}
