package it.unical.fleetgo.backend.Models.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String statusContrattualeVeicolo;
    private Boolean inManutenzione;
    private String nomeAziendaAffiliata;
    private Integer idAziendaAffiliata;
    private LuogoDTO luogoRitiroDeposito;

    public VeicoloDTO(){}

    @JsonIgnore
    public VeicoloDTO(Veicolo veicolo, boolean caricaLuogo) {
        this.idVeicolo = veicolo.getIdVeicolo();
        this.targaVeicolo = veicolo.getTargaVeicolo();
        this.urlImmagine = veicolo.getUrlImmagine();
        this.modello = veicolo.getModello();
        this.tipoDistribuzioneVeicolo = veicolo.getTipoDistribuzioneVeicolo();
        this.livelloCarburante = veicolo.getLivelloCarburante();
        this.inManutenzione = veicolo.getInManutenzione();
        this.statusContrattualeVeicolo = veicolo.getStatusContrattualeVeicolo();
        this.nomeAziendaAffiliata = veicolo.getNomeAziendaAffiliata();
        this.idAziendaAffiliata = veicolo.getIdAziendaAffiliata();

        if(caricaLuogo)
            if(veicolo.getLuogo() != null) this.luogoRitiroDeposito = new LuogoDTO(veicolo.getLuogo());
            else this.luogoRitiroDeposito = null;

        else this.luogoRitiroDeposito = null;
    }
}