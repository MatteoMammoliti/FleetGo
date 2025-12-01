package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaManutenzione;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;

public class RichiestaManutenzioneProxy extends RichiestaManutenzione {

    private final VeicoloDAO veicoloDAO;

    private boolean veicoloCaricato = false;

    public RichiestaManutenzioneProxy(VeicoloDAO veicoloDAO) {
        this.veicoloDAO = veicoloDAO;
    }

    @Override
    public Veicolo getVeicolo() {
        if(!veicoloCaricato){
            Veicolo v = veicoloDAO.getVeicoloDaId(super.getIdVeicolo());
            veicoloCaricato = true;
            super.setVeicolo(v);
        }
        return super.getVeicolo();
    }
}
