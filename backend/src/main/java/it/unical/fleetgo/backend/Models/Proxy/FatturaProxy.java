package it.unical.fleetgo.backend.Models.Proxy;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import it.unical.fleetgo.backend.Persistence.Entity.Fattura;

public class FatturaProxy extends Fattura {

    private final AziendaDAO aziendaDAO;

    private boolean aziendaCaricata = false;

    public FatturaProxy(AziendaDAO aziendaDAO) {
        this.aziendaDAO = aziendaDAO;
    }

    @Override
    public Azienda getAzienda() {
        if(!aziendaCaricata) {
            Azienda azienda = aziendaDAO.getAzienda(super.getIdAzienda());
            super.setAzienda(azienda);
            aziendaCaricata = true;
        }
        return super.getAzienda();
    }
}