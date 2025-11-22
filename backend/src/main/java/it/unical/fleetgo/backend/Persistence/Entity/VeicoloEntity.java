package it.unical.fleetgo.backend.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "veicolo")
public class VeicoloEntity {
    @Id
    @Column(name = "id_veicolo")
    private Integer idVeicolo;

    @Column(name = "targa")
    private String targaVeicolo;

    @Column(name = "immagine_veicolo")
    private String UrlImmagine;

    @Column(name = "modello_veicolo")
    private String modello;

    @Column(name = "tipo_distribuzione_veicolo")
    private String tipoDistribuzioneVeicolo;

    @Column(name = "livello_benzina_veicolo")
    private Integer livelloCarburante;

    @Column(name = "status_condizione_veicolo")
    private String statusCondizioneVeicolo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "veicolo")
    private Set<RichiestaManutenzioneEntity> richiestaManutenzione;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "veicolo")
    private Set<GestioneVeicoloAziendaEntity> gestioneVeicolo;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "veicolo")
    private Set<GestioneVeicoloFleetGoEntity> gestioneVeicoloFleetGo;
}
