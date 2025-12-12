package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
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
    private String nomeAziendaAffiliata;
    private Integer idAziendaAffiliata;
    private LuogoDTO luogoRitiroDeposito;

    public VeicoloDTO(Veicolo veicolo) {
        this.idVeicolo = veicolo.getIdVeicolo();
        this.targaVeicolo = veicolo.getTargaVeicolo();
        this.urlImmagine = veicolo.getUrlImmagine();
        this.modello = veicolo.getModello();
        this.tipoDistribuzioneVeicolo = veicolo.getTipoDistribuzioneVeicolo();
        this.livelloCarburante = veicolo.getLivelloCarburante();
        this.statusCondizioneVeicolo = veicolo.getStatusCondizioneVeicolo();
        this.nomeAziendaAffiliata = veicolo.getNomeAziendaAffiliata();
        this.idAziendaAffiliata = veicolo.getIdAziendaAffiliata();

        if(veicolo.getLuogo() != null) this.luogoRitiroDeposito = new LuogoDTO(veicolo.getLuogo());
        else this.luogoRitiroDeposito = null;
    }
}