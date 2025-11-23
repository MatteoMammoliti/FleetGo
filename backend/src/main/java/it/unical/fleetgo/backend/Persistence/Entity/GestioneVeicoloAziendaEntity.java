package it.unical.fleetgo.backend.Persistence.Entity;

import it.unical.fleetgo.backend.Persistence.Entity.EmbeddableKeys.gestioneVeicoloAziendaFK;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "gestione_veicolo_azienda")
public class GestioneVeicoloAziendaEntity {
    @EmbeddedId
    private gestioneVeicoloAziendaFK id;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idVeicolo")
    @JoinColumn(name = "id_veicolo",referencedColumnName = "id_veicolo")
    private VeicoloEntity veicolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idAzienda")
    @JoinColumn(name = "id_azienda",referencedColumnName = "id_azienda")
    private AziendaEntity azienda;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="id_richiesta_associata",referencedColumnName ="id_richiesta" )
    private  RichiestaNoleggioEntity richiestaNoleggio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "luogo_ritiro_consegna",referencedColumnName = "id_luogo")
    private LuogoAziendaEntity luogo;
}
