package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.FatturaDAO;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import it.unical.fleetgo.backend.Persistence.Entity.Fattura;

public class FatturaProxy extends Fattura {

    private final FatturaDAO fatturaDAO;
    private final AziendaDAO aziendaDAO;

    private boolean costoCaricato = false;
    private boolean aziendaCaricata = false;

    public FatturaProxy(FatturaDAO fatturaDAO, AziendaDAO aziendaDAO) {
        this.fatturaDAO = fatturaDAO;
        this.aziendaDAO = aziendaDAO;
    }

    @Override
    public Integer getCosto() {
        if(!costoCaricato) {
            costoCaricato = true;
            super.setCosto(fatturaDAO.getCosto());
        }
        return super.getCosto();
    }

    @Override
    public Azienda getAzienda() {
        if(!aziendaCaricata) {
            Azienda azienda = aziendaDAO.getAzienda();
            super.setAzienda(azienda);
            aziendaCaricata = true;
        }
        return super.getAzienda();
    }
}