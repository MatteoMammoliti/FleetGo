package it.unical.fleetgo.backend.Models.DTO.Utente;
import it.unical.fleetgo.backend.Models.DTO.*;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.AdminAziendale;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Utente;

import java.util.List;

public class AdminAziendaleDTO extends UtenteDTO {
    private Integer idAziendaGestita;
    private List<RichiestaNoleggioDTO> richiesteNoleggio;
    private List<RichiestaManutenzioneDTO> richiesteManutenzione;
    private List<DipendenteDTO> dipendenti;
    private List<GestioneVeicoloAziendaDTO> veicoloInGestione;
    private List<FatturaDTO> fatture;
    private List<LuogoDTO> luoghiDepositoRitiro;

    public AdminAziendaleDTO() {
        super();
    }

    public AdminAziendaleDTO(AdminAziendale utente) {
        super(utente);
        this.idAziendaGestita = utente.getIdAziendaGestita();
        this.richiesteNoleggio =
                (utente.getRichiesteNoleggio() != null) ?
                utente.getRichiesteNoleggio().stream().map(r -> new RichiestaNoleggioDTO(r, false)).toList()
                : null;

        this.richiesteManutenzione =
                (utente.getRichiesteManutenzione() != null) ?
                utente.getRichiesteManutenzione().stream().map(r -> new RichiestaManutenzioneDTO(r, false)).toList()
                : null;

        this.dipendenti =
                (utente.getDipendenti() != null) ?
                utente.getDipendenti().stream().map(DipendenteDTO::new).toList()
                : null;

        this.veicoloInGestione =
                (utente.getVeicoliInGestione() != null) ?
                utente.getVeicoliInGestione().stream().map(g -> new GestioneVeicoloAziendaDTO(g, false, true)).toList()
                : null;

        this.fatture =
                (utente.getFatture() != null) ?
                utente.getFatture().stream().map(f -> new FatturaDTO(f, false, false)).toList()
                : null;

        this.luoghiDepositoRitiro =
                (utente.getLuoghiDepositoRitiro() != null) ?
                utente.getLuoghiDepositoRitiro().stream().map(LuogoDTO::new).toList()
                : null;
    }
}