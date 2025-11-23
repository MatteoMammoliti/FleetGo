package it.unical.fleetgo.backend.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "richiesta_noleggio")
@Setter
@Getter
public class RichiestaNoleggioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_richiesta")
    private Integer idRichiestaNoleggio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dipendente",referencedColumnName = "id_utente")
    private UtenteEntity utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_azienda",referencedColumnName = "id_azienda")
    private AziendaEntity azienda;

    @Column(name = "data_ritiro")
    private LocalDate dataRitiro;

    @Column(name = "data_consegna")
    private LocalDate dataConsegna;

    @Column(name = "ora_inizio")
    private LocalTime oraInizio;

    @Column(name = "ora_fine")
    private LocalTime oraFine;

    @Column(name = "motivazione")
    private String motivazione;

    @Column(name = "accettata")
    private Boolean richiestaAccettata;


}
