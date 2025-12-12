package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Persistence.Entity.Fattura;
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

    public FatturaDTO(Fattura fattura) {
        this.numeroFattura = fattura.getNumeroFattura();
        this.idAzienda = fattura.getIdAzienda();
        this.meseFattura = fattura.getMeseFattura();
        this.annoFattura = fattura.getAnnoFattura();
        this.fatturaPagata = fattura.getFatturaPagata();
        this.costo = fattura.getCosto();
        this.azienda = new AziendaDTO(fattura.getAzienda());
    }
}