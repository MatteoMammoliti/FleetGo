package it.unical.fleetgo.backend.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "richiesta_manutenzione")
public class RichiestaManutenzioneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_manutenzione")
    private Integer idManutenzione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_azienda",referencedColumnName = "id_utente")
    private UtenteEntity admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veicolo",referencedColumnName = "id_veicolo")
    private VeicoloEntity veicolo;

    @Column(name = "data_richiesta")
    private LocalDate dataRichiesta;

    @Column(name = "tipo_manutenzione")
    private String tipoManutenzione;

    @Column(name = "accettata")
    private Boolean richiestaAccettata;

    @Column(name = "completata")
    private Boolean richiestaCompletata;


}
