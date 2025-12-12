package it.unical.fleetgo.backend.Models.DTO;
import it.unical.fleetgo.backend.Persistence.Entity.GestioneVeicoloAzienda;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GestioneVeicoloAziendaDTO {
    private Integer idVeicolo;
    private VeicoloDTO Veicolo;
    private Integer idAzienda;
    private Integer idLuogo;
    private LuogoDTO luogo;
    private boolean disponbilePerNoleggio;

    public GestioneVeicoloAziendaDTO() {}

    public GestioneVeicoloAziendaDTO(GestioneVeicoloAzienda gestione, boolean caricaVeicolo, boolean caricaLuogo) {
        this.idVeicolo = gestione.getIdVeicolo();

        if(caricaVeicolo) this.Veicolo = new VeicoloDTO(gestione.getVeicolo(), false);
        else this.Veicolo = null;

        this.idAzienda = gestione.getIdAzienda();
        this.idLuogo = gestione.getIdLuogo();
        this.disponbilePerNoleggio=gestione.isDisponbilePerNoleggio();

        if(caricaLuogo) this.luogo = new LuogoDTO(gestione.getLuogo());
        else this.luogo = null;
    }
}