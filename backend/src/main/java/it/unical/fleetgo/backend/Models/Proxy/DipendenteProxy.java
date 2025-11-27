package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.CredenzialiDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaAffiliazioneAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaNoleggioDAO;
import it.unical.fleetgo.backend.Persistence.Entity.ContenitoreCredenziali;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;

import java.util.List;
import java.util.Set;


public class DipendenteProxy extends Dipendente {
    private final RichiestaAffiliazioneAziendaDAO aziendaDAO;
    private final CredenzialiDAO credenzialiDAO;
    private final RichiestaNoleggioDAO noleggioDAO;
    private boolean richiesteNoleggioCaricate = false;
    private boolean idAziendaCaricato= false;
    private boolean credenzialiCaricato = false;

    public DipendenteProxy(RichiestaAffiliazioneAziendaDAO aziendaDAO, CredenzialiDAO credenzialiDAO, RichiestaNoleggioDAO noleggioDAO) {
        this.aziendaDAO = aziendaDAO;
        this.credenzialiDAO = credenzialiDAO;
        this.noleggioDAO = noleggioDAO;
    }

    @Override
    public List<RichiestaNoleggio> getRichiesteNoleggio() {
        if(!richiesteNoleggioCaricate) {
            richiesteNoleggioCaricate = true;
            super.setRichiesteNoleggio(noleggioDAO.getRichiesteNoleggioAziendaDaAccettare(super.getIdAziendaAffiliata()));
        }
        return super.getRichiesteNoleggio();
    }
    @Override
    public Integer getIdAziendaAffiliata(){
        if(!idAziendaCaricato) {
            idAziendaCaricato = true;
            super.setIdAziendaAffiliata((Integer) aziendaDAO);
        }
        return super.getIdAziendaAffiliata();
    }
    @Override
    public ContenitoreCredenziali getCredenziali() {
        if(!credenzialiCaricato) {
            credenzialiCaricato = true;
            super.setCredenziali(credenzialiDAO.getCredenzialiUtente(getIdAziendaAffiliata()));
        }
        return super.getCredenziali();
    }
}
