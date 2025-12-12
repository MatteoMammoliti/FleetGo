package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LuogoDTO {
    private Integer idLuogo;
    private Integer idAzienda;
    private String nomeLuogo;
    private Float longitudine;
    private Float latitudine;

    public LuogoDTO() {}

    public LuogoDTO(LuogoAzienda luogo) {
        this.idLuogo = luogo.getIdLuogo();
        this.idAzienda = luogo.getIdAzienda();
        this.nomeLuogo = luogo.getNomeLuogo();
        this.longitudine = luogo.getLongitudine();
        this.latitudine = luogo.getLatitudine();
    }
}
