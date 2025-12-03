package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModificaDatiUtenteDTO
{
    String nome;
    String cognome;
    String data;
    String email;
    String nomeAzienda;
    String sedeAzienda;
    String pIva;
    Integer idUtente;
}
