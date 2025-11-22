package it.unical.fleetgo.backend.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "azienda")
@Getter
@Setter
public class AziendaEntity {

    @Id
    @Column(name = "id_azienda")
    private int idAzienda;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_admin_azienda",referencedColumnName = "id_utente")
    private UtenteEntity admin;

    @Column(name = "sede_azienda")
    private String sedeAzienda;
    @Column(name = "nome_azienda")
    private String nomeAzienda;
    @Column(name = "p_iva")
    private String pIva;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "azienda")
    private Set<RichiestaAffiliazioneAziendaEntity> richieste;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "azienda")
    private Set<FatturaEntity> fatture;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "azienda")
    private Set<RichiestaNoleggioEntity> richiesteNoleggio;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "azienda")
    private Set<GestioneVeicoloAziendaEntity> gestioneVeicolo;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "azienda")
    private Set<LuogoAziendaEntity> luoghi;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "azienda")
    private Set<GestioneVeicoloFleetGoEntity> gestioneVeicoloFleetGo;
}
