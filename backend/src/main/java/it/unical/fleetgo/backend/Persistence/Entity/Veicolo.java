package it.unical.fleetgo.backend.Persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Veicolo {
    private Integer idVeicolo;
    private String targaVeicolo;
    private String urlImmagine;
    private Integer idModello;
    private String nomeModello;
    private String tipoDistribuzioneVeicolo;
    private Integer livelloCarburante;
    private String statusContrattualeVeicolo;
    private String nomeAziendaAffiliata;
    private Integer idAziendaAffiliata;
    private Boolean inManutenzione;
    private LuogoAzienda luogo;
}