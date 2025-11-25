package it.unical.fleetgo.backend.Persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Veicolo {
    private Integer idVeicolo;
    private String targaVeicolo;
    private String UrlImmagine;
    private String modello;
    private String tipoDistribuzioneVeicolo;
    private Integer livelloCarburante;
    private String statusCondizioneVeicolo;
}
