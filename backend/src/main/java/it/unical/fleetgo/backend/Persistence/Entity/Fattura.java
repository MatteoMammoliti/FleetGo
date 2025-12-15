package it.unical.fleetgo.backend.Persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fattura {
    private Integer numeroFattura;
    private Integer idAzienda;
    private Integer annoFattura;
    private Integer meseFattura;
    private Integer fleetGo;
    private Azienda azienda;
    private Float costo;
    private Boolean fatturaPagata;
    private Integer idOffertaApplicata;
    private Offerta offertaApplicata;
}