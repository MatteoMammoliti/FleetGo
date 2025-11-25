package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeicoloDTO {
    private Integer idVeicolo;
    private String targaVeicolo;
    private String urlImmagine;
    private String modello;
    private String tipoDistribuzioneVeicolo;
    private Integer livelloCarburante;
    private String statusCondizioneVeicolo;
    private Integer idAziendaAssociato;
    private LuogoDTO luogoRitiroDeposito;

}
