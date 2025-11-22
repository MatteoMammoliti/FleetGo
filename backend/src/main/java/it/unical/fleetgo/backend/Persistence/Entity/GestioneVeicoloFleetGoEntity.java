package it.unical.fleetgo.backend.Persistence.Entity;

import it.unical.fleetgo.backend.Persistence.Entity.EmbeddableKeys.GestioneVeicoloFleetFK;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gestione_veicolo_fleetgo")
public class GestioneVeicoloFleetGoEntity {

    @EmbeddedId
    private GestioneVeicoloFleetFK gestioneVeicoloFleetFK;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idFleet")
    @JoinColumn(name = "id_fleet",referencedColumnName = "id_utente")
    private UtenteEntity fleetgo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idVeicolo")
    @JoinColumn(name = "id_veicolo",referencedColumnName = "id_veicolo")
    private VeicoloEntity veicolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_azienda",referencedColumnName = "id_azienda")
    private AziendaEntity azienda;

    @Column(name = "noleggiato")
    private boolean noleggiata;

}
