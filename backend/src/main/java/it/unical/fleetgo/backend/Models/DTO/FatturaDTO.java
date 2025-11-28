package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FatturaDTO {
    private Integer numeroFattura;
    private Integer idFleetGo;
    private Integer idAzienda;
    private Integer meseFattura;
    private Integer annoFattura;
    private boolean fatturaPagata;
    private Integer costo;
    private AziendaDTO azienda;
}
