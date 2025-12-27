package it.unical.fleetgo.backend.Models.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unical.fleetgo.backend.Persistence.Entity.Modello;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelloDTO {

    private Integer idModello;
    private String nomeModello;
    private String urlImmagine;

    public ModelloDTO() {}

    @JsonIgnore
    public ModelloDTO(Modello modello) {
        this.idModello = modello.getIdModello();
        this.nomeModello = modello.getNomeModello();
        this.urlImmagine = modello.getUrlImmagine();
    }
}