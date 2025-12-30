package it.unical.fleetgo.backend.Models.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public RichiestaManutenzioneDTO(RichiestaManutenzione richiesta, boolean caricaVeicolo) {
        this.idManutenzione = richiesta.getIdManutenzione();
        this.idAdminAzienda = richiesta.getIdAdmin();
        this.idVeicolo = richiesta.getIdVeicolo();
        this.dataRichiesta = richiesta.getDataRichiesta();
        this.tipoManutenzione = richiesta.getTipoManutenzione();
        this.accettata = richiesta.getRichiestaAccettata();
        this.completata = richiesta.getRichiestaCompletata();

        if(caricaVeicolo) this.veicolo = new VeicoloDTO(richiesta.getVeicolo(), false);
        else this.veicolo = null;
    }
}