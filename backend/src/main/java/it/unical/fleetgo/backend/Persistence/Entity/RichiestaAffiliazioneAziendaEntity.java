package it.unical.fleetgo.backend.Persistence.Entity;

import it.unical.fleetgo.backend.Persistence.Entity.EmbeddableKeys.RichiestaAffiliazioneFK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "richiesta_affiliazione_azienda")
@Getter
@Setter
public class RichiestaAffiliazioneAziendaEntity {
    @EmbeddedId
    private RichiestaAffiliazioneFK idAziendaDipendente;
    @ManyToOne
    @MapsId("idUtente")
    @JoinColumn(name = "id_dipendente", referencedColumnName = "id_utente")
    private UtenteEntity utente;
    @ManyToOne
    @MapsId("idAzienda")
    @JoinColumn(name = "id_azienda")
    private AziendaEntity azienda;
    @Column(name = "accettata")
    private boolean accettata;
    @Column(name = "data_accettazione")
    private LocalDate dataAccettazione;
}
