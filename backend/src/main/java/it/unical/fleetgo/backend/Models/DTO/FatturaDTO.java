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
    private Float costo;
    private AziendaDTO azienda;
    private Integer idOffertaApplicata;
    private OffertaDTO offertaApplicata;

    public FatturaDTO(Fattura fattura, boolean caricareAzienda, boolean caricaOfferta) {
        this.numeroFattura = fattura.getNumeroFattura();
        this.idAzienda = fattura.getIdAzienda();
        this.meseFattura = fattura.getMeseFattura();
        this.annoFattura = fattura.getAnnoFattura();
        this.fatturaPagata = fattura.getFatturaPagata();
        this.costo = fattura.getCosto();

        if(caricareAzienda) this.azienda = new AziendaDTO(fattura.getAzienda());
        else this.azienda = null;

        if(caricaOfferta) this.offertaApplicata = new OffertaDTO(fattura.getOffertaApplicata());
        else this.offertaApplicata = null;
    }

    public FatturaDTO(FatturaDaGenerareDTO fattura) {
        this.idAzienda = fattura.getIdAzienda();
        this.meseFattura = Integer.parseInt(fattura.getMese());
        this.annoFattura = Integer.parseInt(fattura.getAnno());
        this.fatturaPagata = false;
        this.costo = fattura.getCostoTotale();
        this.idOffertaApplicata = fattura.getIdOffertaApplicata();
        this.offertaApplicata = null;
    }
}