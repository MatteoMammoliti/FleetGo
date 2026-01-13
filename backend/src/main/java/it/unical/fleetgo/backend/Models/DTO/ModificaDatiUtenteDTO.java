package it.unical.fleetgo.backend.Models.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModificaDatiUtenteDTO {
    String nome;
    String cognome;
    String data;
    String email;
    String nomeAzienda;
    String sedeAzienda;

    @JsonProperty("pIva")
    String pIva;

    Integer idUtente;

    public ModificaDatiUtenteDTO(String nome,
                                 String cognome,
                                 String data,
                                 String email,
                                 String nomeAzienda,
                                 String sedeAzienda,
                                 String pIva,
                                 Integer idUtente){
        this.nome=nome;
        this.cognome=cognome;
        this.data=data;
        this.email=email;
        this.nomeAzienda=nomeAzienda;
        this.sedeAzienda=sedeAzienda;
        this.pIva=pIva;
        this.idUtente=idUtente;
    };
}