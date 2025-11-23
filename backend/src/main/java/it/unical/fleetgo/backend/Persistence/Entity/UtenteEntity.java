package it.unical.fleetgo.backend.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "utente")
public class UtenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utente")
    private Integer idUtente;

    @Column(name="nome_utente")
    private String nomeUtente;

    @Column(name="cognome")
    private String cognomeUtente;

    @Column (name = "data_nascita")
    private LocalDate dataNascitaUtente;

    @Column(name="tipo_utente")
    private String tipoUtente;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "utente")
    private Set<RichiestaAffiliazioneAziendaEntity> richieste;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "fleetGo")
    private Set<FatturaEntity> fatture;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "utente")
    private Set<RichiestaNoleggioEntity> richiesteNoleggio;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "admin")
    private Set<RichiestaManutenzioneEntity> richiesteManutenzione;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "fleetgo")
    private Set<GestioneVeicoloFleetGoEntity> gestioneVeicoloFleetGo;
}
