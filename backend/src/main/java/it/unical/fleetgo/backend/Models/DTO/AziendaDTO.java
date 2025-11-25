package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AziendaDTO {
    private Integer idAzienda;
    private Integer idAdminAzienda;
    private String nomeAzienda;
    private String sedeAzienda;
    private String pIva;
    private List<UtenteDTO> dipendentiAzienda;
    private List<VeicoloDTO> autoGestiteDaAzienda;
}
