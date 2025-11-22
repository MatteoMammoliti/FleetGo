package it.unical.fleetgo.backend.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "richiesta_manutenzione")
public class RichiestaManutenzioneEntity {
    @Id
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
