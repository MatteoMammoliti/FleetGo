package it.unical.fleetgo.backend.Persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LuogoAzienda {
    private Integer idLuogo;
    private Azienda azienda;
    private String nomeLuogo;
    private float longitudine;
    private float latitudine;
}
