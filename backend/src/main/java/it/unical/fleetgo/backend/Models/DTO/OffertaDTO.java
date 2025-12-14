package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Persistence.Entity.Offerta;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class OffertaDTO {

    private Integer idOfferta;
    private String nomeOfferta;
    private String descrizioneOfferta;
    private String scadenza;
    private Integer percentualeSconto;
    private String immagineCopertina;

    public OffertaDTO() {}

    public OffertaDTO(Offerta offerta) {
        this.idOfferta = offerta.getIdOfferta();
        this.nomeOfferta = offerta.getNomeOfferta();
        this.descrizioneOfferta = offerta.getDescrizioneOfferta();
        this.scadenza = offerta.getScadenza().toString();
        this.percentualeSconto = offerta.getPercentualeSconto();
        this.immagineCopertina = offerta.getImmagineCopertina();
    }
}