package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.CredenzialiDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaAffiliazioneAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaNoleggioDAO;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import java.util.List;

public class DipendenteProxy extends Dipendente {
    private final RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDao;
    private final CredenzialiDAO credenzialiDAO;
    private boolean idAziendaCaricato= false;
    private boolean credenzialiCaricato = false;

    public DipendenteProxy(RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDao, CredenzialiDAO credenzialiDAO) {
        this.richiestaAffiliazioneAziendaDao = richiestaAffiliazioneAziendaDao;
        this.credenzialiDAO = credenzialiDAO;
    }

    @Override
    public Integer getIdAziendaAffiliata(){
        if(!idAziendaCaricato) {
            idAziendaCaricato = true;
            super.setIdAziendaAffiliata(richiestaAffiliazioneAziendaDao.getIdAziendaDipendente(this.getIdUtente()));
        }
        return super.getIdAziendaAffiliata();
    }

    @Override
    public CredenzialiUtente getCredenziali() {
        if(!credenzialiCaricato) {
            credenzialiCaricato = true;
            super.setCredenziali(credenzialiDAO.getCredenzialiUtenteById(this.getIdUtente()));
        }
        return super.getCredenziali();
    }
}