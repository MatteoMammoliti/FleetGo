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

    public GestioneVeicoloAziendaDTO(GestioneVeicoloAzienda gestione) {
        this.idVeicolo = gestione.getIdVeicolo();
        this.Veicolo = new VeicoloDTO(gestione.getVeicolo());
        this.idAzienda = gestione.getIdAzienda();
        this.idLuogo = gestione.getIdLuogo();
        this.disponbilePerNoleggio=gestione.isDisponbilePerNoleggio();
        this.luogo = new LuogoDTO(gestione.getLuogo());
    }
}