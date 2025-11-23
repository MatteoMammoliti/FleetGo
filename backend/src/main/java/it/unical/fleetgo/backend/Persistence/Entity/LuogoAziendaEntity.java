package it.unical.fleetgo.backend.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "luogo_azienda")
public class LuogoAziendaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_luogo")
    private Integer idLuogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_azienda",referencedColumnName = "id_azienda")
    private AziendaEntity azienda;

    @Column(name = "nome_luogo")
    private String nomeLuogo;

    @Column(name = "longitudine")
    private float longitudine;

    @Column(name = "latitudine")
    private float latitudine;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "luogo")
    private Set<GestioneVeicoloAziendaEntity> gestioneVeicolo;
}
