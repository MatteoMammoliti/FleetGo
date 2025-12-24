package it.unical.fleetgo.backend.Persistence.Entity.Utente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredenzialiUtente {
    private Integer idUtente;
    private String password;
    private String email;
    private String imgPatente;
}