package it.unical.fleetgo.backend.Models.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AziendaDTO {
    private Integer idAzienda;
    private Integer idAdminAzienda;
    private String nomeAzienda;
    private Integer sedeAzienda;
    private String pIva;

    public AziendaDTO() {}

    @JsonIgnore
    public AziendaDTO(Azienda azienda) {
        this.idAzienda = azienda.getIdAzienda();
        this.idAdminAzienda = azienda.getIdAdmin();
        this.nomeAzienda = azienda.getNomeAzienda();
        this.sedeAzienda = azienda.getSedeAzienda();
        this.pIva = azienda.getPIva();
    }
}