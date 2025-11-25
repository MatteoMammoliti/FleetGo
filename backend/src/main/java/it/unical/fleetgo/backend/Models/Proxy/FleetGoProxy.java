package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.FatturaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiesteManutenzioneDAO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.Entity.*;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.FleetGo;

import java.util.Set;

public class FleetGoProxy extends FleetGo {
    private final RichiesteManutenzioneDAO manutenzioneDAO;
    private final VeicoloDAO veicoloDAO;
    private final AziendaDAO aziendaDAO;
    private final FatturaDAO fatturaDAO;

    private boolean richiesteManutenzioneCaricate=false;
    private boolean veicoliCaricati=false;
    private boolean aziendeCaricate=false;
    private boolean fattureCaricate=false;

    public FleetGoProxy(RichiesteManutenzioneDAO manutenzioneDAO, VeicoloDAO veicoloDao, AziendaDAO aziendaDao, FatturaDAO fatturaDao) {
        this.manutenzioneDAO = manutenzioneDAO;
        this.veicoloDAO = veicoloDao;
        this.aziendaDAO = aziendaDao;
        this.fatturaDAO = fatturaDao;
    }

    @Override
    public Set<RichiestaManutenzione> getRichiesteManutenzione() {
        if(!richiesteManutenzioneCaricate){
            richiesteManutenzioneCaricate=true;
            super.setRichiesteManutenzione((Set<RichiestaManutenzione>) manutenzioneDAO);
        }
        return super.getRichiesteManutenzione();
    }
    @Override
    public Set<Veicolo> getVeicoloDisponibili() {
        if(!veicoliCaricati){
            veicoliCaricati=true;
            super.setVeicoloDisponibili((Set<Veicolo>) veicoloDAO);
        }
        return super.getVeicoloDisponibili();
    }
    @Override
    public Set<Azienda> getAziende(){
        if(!aziendeCaricate){
            aziendeCaricate=true;
            super.setAziende((Set<Azienda>) aziendaDAO);
        }
        return super.getAziende();
    }
    @Override
    public Set<Fattura> getFattureEmesse(){
        if(!fattureCaricate){
            fattureCaricate=true;
            super.setFattureEmesse((Set<Fattura>) fatturaDAO);
        }
        return super.getFattureEmesse();
    }
}
