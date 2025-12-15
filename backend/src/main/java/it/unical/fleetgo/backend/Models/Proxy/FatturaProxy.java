package it.unical.fleetgo.backend.Models.Proxy;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.OffertaDAO;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import it.unical.fleetgo.backend.Persistence.Entity.Fattura;
import it.unical.fleetgo.backend.Persistence.Entity.Offerta;

public class FatturaProxy extends Fattura {

    private final AziendaDAO aziendaDAO;
    private final OffertaDAO offertaDAO;

    private boolean aziendaCaricata = false;
    private boolean offertCaricata = false;

    public FatturaProxy(AziendaDAO aziendaDAO, OffertaDAO offertaDAO) {

        this.aziendaDAO = aziendaDAO;
        this.offertaDAO = offertaDAO;
    }

    @Override
    public Azienda getAzienda() {
        if(!aziendaCaricata) {
            Azienda azienda = aziendaDAO.getAziendaById(super.getIdAzienda());
            super.setAzienda(azienda);
            aziendaCaricata = true;
        }
        return super.getAzienda();
    }

    @Override
    public Offerta getOffertaApplicata() {
        if(!offertCaricata) {
            Offerta offerta = offertaDAO.getOffertaById(super.getIdOffertaApplicata());
            super.setOffertaApplicata(offerta);
            offertCaricata = true;
        }
        return super.getOffertaApplicata();
    }
}