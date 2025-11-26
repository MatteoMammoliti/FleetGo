package it.unical.fleetgo.backend.Persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContenitoreCredenziali {

    private String email;
    private String urlImmagine;
    private boolean patenteAccetta;

    public ContenitoreCredenziali(){}

    public ContenitoreCredenziali(String email, String urlImmagine, boolean patenteAccetta) {
        this.email = email;
        this.urlImmagine = urlImmagine;
        this.patenteAccetta = patenteAccetta;
            }

        public boolean getPatenteAccetta() {
            return patenteAccetta;
        }
}
