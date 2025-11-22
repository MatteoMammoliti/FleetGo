package it.unical.fleetgo.backend.Persistence.Entity;

import it.unical.fleetgo.backend.Persistence.Entity.EmbeddableKeys.FatturaFk;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fattura")
@Getter
@Setter
public class FatturaEntity {

    @EmbeddedId
    private FatturaFk pkFattura;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idFleet")
    @JoinColumn(name = "id_fleetgo",referencedColumnName = "id_utente")
    private UtenteEntity fleetGo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idAzienda")
    @JoinColumn(name = "id_azienda",referencedColumnName = "id_azienda")
    private AziendaEntity azienda;

    @Column(name = "costo")
    private Integer costo;

    @Column(name = "fattura_pagata")
    private Boolean fatturaPagata;
}
