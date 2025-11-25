package it.unical.fleetgo.backend.Persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredenzialiUtente {
    private Integer idUtente;
    private String password;
    private String email;
    private boolean patente;
    private String imgPatente;
}
