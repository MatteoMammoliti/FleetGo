package it.unical.fleetgo.backend.Models.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContenitoreCredenzialiDTO {
    private String email;
    private String urlImmagine;
    private boolean patenteAccetta;

    public ContenitoreCredenzialiDTO(){}

    public ContenitoreCredenzialiDTO(String email, String urlImmagine, boolean patenteAccetta) {
        this.email = email;
        this.urlImmagine = urlImmagine;
        this.patenteAccetta = patenteAccetta;
    }

    public boolean getPatenteAccetta() {
        return patenteAccetta;
    }
}
