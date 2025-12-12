package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FatturaDaGenerareDTO {
    Integer idAzienda;
    String nomeAzienda;
    String anno;
    String mese;
    Integer numeroNoleggi;
    Integer costoTotale;

    public FatturaDaGenerareDTO(Integer idAzienda,
                                String nomeAzienda,
                                String anno,
                                String mese,
                                Integer numeroNoleggi,
                                Integer costoTotale) {
        this.idAzienda = idAzienda;
        this.nomeAzienda = nomeAzienda;
        this.anno = anno;
        this.mese = mese;
        this.numeroNoleggi = numeroNoleggi;
        this.costoTotale = costoTotale;
    }
}
