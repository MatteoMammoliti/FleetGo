package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.FatturaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiesteManutenzioneDAO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.Entity.*;

import java.util.HashSet;
import java.util.Set;

public class FleetGoProxy extends FleetGo {
    private RichiesteManutenzioneDAO manutenzioneDAO;
    private VeicoloDAO veicoloDAO;
    private AziendaDAO aziendaDAO;
    private FatturaDAO fatturaDAO;

    private boolean richiesteManutenzioneCaricate=false;
    private boolean veicoliCaricati=false;
    private boolean aziendeCaricate=false;
    private boolean fattureCaricate=false;

    public FleetGoProxy(VeicoloDAO veicoloDao,AziendaDAO aziendaDao,FatturaDAO fatturaDao) {
        this.veicoloDAO = veicoloDao;
        this.aziendaDAO = aziendaDao;
        this.fatturaDAO = fatturaDao;
    }

    @Override
    public Set<RichiestaManutenzione> getRichiesteManutenzione() {
        if(!richiesteManutenzioneCaricate){
            Set<RichiestaManutenzione> manutenzioni = manutenzioneDAO;
            richiesteManutenzioneCaricate=true;
            super.setRichiesteManutenzione(manutenzioni);
        }
        return super.getRichiesteManutenzione();
    }
    @Override
    public Set<Veicolo> getVeicoloDisponibili() {
        if(!veicoliCaricati){
            Set<Veicolo> veicoli= veicoloDAO;
            veicoliCaricati=true;
            super.setVeicoloDisponibili(veicoli);
        }
        return super.getVeicoloDisponibili();
    }
    @Override
    public Set<Azienda> getAziende(){
        if(!aziendeCaricate){
            Set<Azienda> aziende = aziendaDAO;
            aziendeCaricate=true;
            super.setAziende(aziende);
        }
        return super.getAziende();
    }
    @Override
    public Set<Fattura> getFattureEmesse(){
        if(!fattureCaricate){
            Set<Fattura> fatture = fatturaDAO;
            fattureCaricate=true;
            super.setFattureEmesse(fatture);
        }
        return super.getFattureEmesse();
    }
}
