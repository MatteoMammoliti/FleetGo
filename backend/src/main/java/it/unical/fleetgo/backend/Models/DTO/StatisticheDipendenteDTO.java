package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;

@Setter
@Getter
public class StatisticheDipendenteDTO {
    private Integer viaggiMensili;
    private Float oreTotaliDiGuida;

    public StatisticheDipendenteDTO() {}
    public StatisticheDipendenteDTO(Integer viaggiMensili, Float oreTotaliDiGuida) {
        this.viaggiMensili = viaggiMensili;
        this.oreTotaliDiGuida = oreTotaliDiGuida;
    }
}
