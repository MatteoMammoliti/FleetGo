package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Utente;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;

public class RichiestaNoleggioProxy extends RichiestaNoleggio {

    private final UtenteDAO utenteDAO;
    private final VeicoloDAO veicoloDAO;
    private boolean utenteCaricato = false;
    private boolean veicoloCaricato=false;

    public RichiestaNoleggioProxy(UtenteDAO utenteDAO,VeicoloDAO veicoloDAO) {
        this.utenteDAO = utenteDAO;
        this.veicoloDAO = veicoloDAO;
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
    @Override
    public Veicolo getVeicolo() {
        if (!veicoloCaricato) {
            Veicolo veicolo = veicoloDAO.getVeicoloDaId(this.getIdVeicolo());
            if (veicolo != null) {
                veicoloCaricato=true;
                super.setVeicolo(veicolo);
            }
        }
        return super.getVeicolo();
    }
}