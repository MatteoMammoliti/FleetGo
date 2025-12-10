package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.LuogoAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.Entity.GestioneVeicoloAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;

public class GestioneVeicoloAziendaProxy extends GestioneVeicoloAzienda {

    private final VeicoloDAO veicoloDAO;
    private final LuogoAziendaDAO luogoDAO;
    private boolean veicoloCaricato = false;
    private boolean luogoCaricato = false;

    public GestioneVeicoloAziendaProxy(VeicoloDAO veicoloDAO, LuogoAziendaDAO luogoDAO) {
        this.veicoloDAO = veicoloDAO;
        this.luogoDAO = luogoDAO;
    }

    @Override
    public Veicolo getVeicolo() {
        if(!veicoloCaricato){
            Veicolo v = veicoloDAO.getVeicoloDaId(
                    super.getIdVeicolo()
            );
            veicoloCaricato = true;
            super.setVeicolo(v);
        }
        return super.getVeicolo();
    }

    @Override
    public LuogoAzienda getLuogo() {
        if(!luogoCaricato) {
            LuogoAzienda l = luogoDAO.getLuogo(
                    super.getIdLuogo()
            );
            luogoCaricato = true;
            super.setLuogo(l);
        }
        return super.getLuogo();
    }
}