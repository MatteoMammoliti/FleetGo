package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Persistence.Entity.Fattura;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FatturaDTO {
    private Integer numeroFattura;
    private Integer idAzienda;
    private Integer meseFattura;
    private Integer annoFattura;
    private boolean fatturaPagata;
    private Integer costo;
    private AziendaDTO azienda;

    public FatturaDTO(Fattura fattura, boolean caricareAzienda) {
        this.numeroFattura = fattura.getNumeroFattura();
        this.idAzienda = fattura.getIdAzienda();
        this.meseFattura = fattura.getMeseFattura();
        this.annoFattura = fattura.getAnnoFattura();
        this.fatturaPagata = fattura.getFatturaPagata();
        this.costo = fattura.getCosto();

        if(caricareAzienda) this.azienda = new AziendaDTO(fattura.getAzienda());
        else this.azienda = null;
    }

    public FatturaDTO(FatturaDaGenerareDTO fattura) {
        this.idAzienda = fattura.getIdAzienda();
        this.meseFattura = Integer.parseInt(fattura.getMese());
        this.annoFattura = Integer.parseInt(fattura.getAnno());
        this.fatturaPagata = false;
        this.costo = fattura.getCostoTotale();
    }
}