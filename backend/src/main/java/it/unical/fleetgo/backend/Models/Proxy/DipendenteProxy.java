package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Models.ContenitoreCredenziali;
import it.unical.fleetgo.backend.Persistence.DAO.CredenzialiDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaAffiliazioneAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaNoleggioDAO;
import it.unical.fleetgo.backend.Persistence.Entity.Dipendente;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;

import java.util.Set;


public class DipendenteProxy extends Dipendente {
    private RichiestaAffiliazioneAziendaDAO aziendaDAO;
    private CredenzialiDAO credenzialiDAO;
    private RichiestaNoleggioDAO noleggioDAO;
    private boolean richiesteNoleggioCaricate = false;
    private boolean idAziendaCaricato= false;
    private boolean credenzialiCaricato = false;

    public DipendenteProxy(RichiestaAffiliazioneAziendaDAO aziendaDAO, CredenzialiDAO credenzialiDAO, RichiestaNoleggioDAO noleggioDAO) {
        this.aziendaDAO = aziendaDAO;
        this.credenzialiDAO = credenzialiDAO;
        this.noleggioDAO = noleggioDAO;
    }

    @Override
    public Set<RichiestaNoleggio> getRichiesteNoleggio() {
        if(!richiesteNoleggioCaricate) {
            Set<RichiestaNoleggio> richieste = noleggioDAO;
            richiesteNoleggioCaricate = true;
            super.setRichiesteNoleggio(richieste);
        }
        return super.getRichiesteNoleggio();
    }
    @Override
    public Integer getIdAziendaAffiliata(){
        if(!idAziendaCaricato) {
            Integer idAzienda= aziendaDAO;
            idAziendaCaricato = true;
            super.setIdAziendaAffiliata(idAzienda);
        }
        return super.getIdAziendaAffiliata();
    }
    @Override
    public ContenitoreCredenziali getCredenziali() {
        if(!credenzialiCaricato) {
            ContenitoreCredenziali credenziali = credenzialiDAO;
            credenzialiCaricato = true;
            super.setCredenziali(credenziali);
        }
        return super.getCredenziali();
    }

}
