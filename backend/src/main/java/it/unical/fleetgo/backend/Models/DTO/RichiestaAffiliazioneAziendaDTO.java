package it.unical.fleetgo.backend.Models.DTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaAffiliazioneAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichiestaAffiliazioneAziendaDTO {
    private DipendenteDTO utente;
    private Integer idUtente;
    private Integer idAzienda;
    private boolean accettata;
    private String dataAccettazione;

    public RichiestaAffiliazioneAziendaDTO(){}

    public RichiestaAffiliazioneAziendaDTO(RichiestaAffiliazioneAzienda richiesta, boolean caricaUtente) {
        this.idAzienda = richiesta.getIdAzienda();
        this.idUtente = richiesta.getIdUtente();
        this.accettata = richiesta.isAccettata();

        if(caricaUtente) this.utente = new DipendenteDTO(richiesta.getUtente());
        else this.utente = null;
    }
}