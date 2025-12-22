package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContenitoreDatiAzienda {
    private Integer idAzienda;
    private String nomeAzienda;
    private String nomeSedeAzienda;
    private Integer totaleVeicoliAzienda;
    private Integer totaleDipendentiAzienda;
}
