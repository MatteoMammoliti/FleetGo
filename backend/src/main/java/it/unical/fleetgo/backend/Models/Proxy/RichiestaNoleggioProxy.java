package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Utente;

public class RichiestaNoleggioProxy extends RichiestaNoleggio {

    private final UtenteDAO utenteDAO;
    private boolean utenteCaricato = false;

    public RichiestaNoleggioProxy(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    @Override
    public Utente getUtente() {
        if (!utenteCaricato) {
            Utente utente = utenteDAO.getDipendenteDaId(this.getIdUtente());
            if (utente != null) {
                utenteCaricato = true;
                super.setUtente(utente);
            }
        }
        return super.getUtente();
    }
}