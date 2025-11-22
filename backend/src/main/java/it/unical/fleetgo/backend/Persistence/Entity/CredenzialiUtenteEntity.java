package it.unical.fleetgo.backend.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "credenziali_utente")
public class CredenzialiUtenteEntity {

    @Id
    @Column(name="id_utente")
    private String idUtente;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "id_utente")
    private UtenteEntity utente;

    @Column(name = "password")
    private String password;
    @Column(name="email")
    private String email;

    @Column(name = "patente")
    private boolean patente;
    @Column(name = "immagine_patente")
    private String imgPatente;

}
