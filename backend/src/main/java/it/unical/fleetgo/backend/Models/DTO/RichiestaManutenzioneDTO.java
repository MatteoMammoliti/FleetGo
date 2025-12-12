package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Persistence.Entity.RichiestaManutenzione;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichiestaManutenzioneDTO {
    private Integer idManutenzione;
    private Integer idAdminAzienda;
    private Integer idVeicolo;
    private String dataRichiesta;
    private String tipoManutenzione;
    private Boolean accettata;
    private Boolean completata;
    private VeicoloDTO veicolo;

    public RichiestaManutenzioneDTO() {}
}