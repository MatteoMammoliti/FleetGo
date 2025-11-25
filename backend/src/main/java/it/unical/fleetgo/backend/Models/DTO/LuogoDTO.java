package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LuogoDTO {
    private Integer idLuogo;
    private Integer idAzienda;
    private String nomeLuogo;
    private float longitudine;
    private float latitudine;
}
