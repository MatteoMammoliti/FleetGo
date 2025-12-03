package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContenitoreDatiUtente {
    String email;
    String nomeUtente;
    String cognomeUtente;
    String dataNascitaUtente;
    String nomeAzienda;
    String sedeAzienda;
    String pIva;

    public ContenitoreDatiUtente(){}
    public ContenitoreDatiUtente(String email, String nomeUtente, String cognomeUtente, String dataNascitaUtente, String nomeAzienda, String sedeAzienda, String pIva){
        this.email=email;
        this.nomeUtente=nomeUtente;
        this.cognomeUtente=cognomeUtente;
        this.dataNascitaUtente=dataNascitaUtente;
        this.nomeAzienda=nomeAzienda;
        this.sedeAzienda=sedeAzienda;
        this.pIva=pIva;
    }
}
