package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.Entity.GestioneVeicoloAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;

public class GestioneVeicoloAziendaProxy extends GestioneVeicoloAzienda {

    private final VeicoloDAO veicoloDAO;

    private boolean veicoloCaricato = false;

    public GestioneVeicoloAziendaProxy(VeicoloDAO veicoloDAO) {
        this.veicoloDAO = veicoloDAO;
    }

    @Override
    public Veicolo getVeicolo() {
        if(!veicoloCaricato){
            Veicolo v = veicoloDAO.getVeicolo();
            veicoloCaricato = true;
            super.setVeicolo(v);
        }
        return super.getVeicolo();
    }


}
