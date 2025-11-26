package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.*;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.AdminAziendale;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdminAziendaleProxy extends AdminAziendale {
    private final AziendaDAO aziendaDAO;
    private final RichiestaNoleggioDAO richiestaNoleggioDAO;
    private final RichiesteManutenzioneDAO richiesteManutenzioneDAO;
    private final RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneDAO;
    private final GestioneVeicoloAziendaDAO  gestioneVeicoloAziendaDAO;
    private final FatturaDAO fatturaDAO;
    private final LuogoAziendaDAO luogoAziendaDAO;

    private boolean aziendaCaricata= false;
    private boolean richiesteNoleggioCaricate = false;
    private boolean richiesteManutenzioniCaricate= false;
    private boolean dipendentiCaricati = false;
    private boolean gestioneVeicoloCaricate= false;
    private boolean fatturaCaricate= false;
    private boolean luogoCaricate= false;
    private boolean richiesteAffiliazioneCaricate= false;

    public AdminAziendaleProxy(AziendaDAO aziendaDAO,RichiestaNoleggioDAO richiestaNoleggioDAO,
                               RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO,GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO,
                               FatturaDAO fatturaDAO,LuogoAziendaDAO luogoAziendaDAO,RichiesteManutenzioneDAO richiesteManutenzioneDAO) {
        this.aziendaDAO = aziendaDAO;
        this.richiestaNoleggioDAO = richiestaNoleggioDAO;
        this.richiestaAffiliazioneDAO = richiestaAffiliazioneAziendaDAO;
        this.fatturaDAO = fatturaDAO;
        this.gestioneVeicoloAziendaDAO = gestioneVeicoloAziendaDAO;
        this.luogoAziendaDAO = luogoAziendaDAO;
        this.richiesteManutenzioneDAO = richiesteManutenzioneDAO;
    }

    @Override
    public Integer getIdAziendaGestita(){
        if(!aziendaCaricata){
            aziendaCaricata= true;
            super.setIdAziendaGestita((Integer) aziendaDAO);
        }
        return super.getIdAziendaGestita();
    }
    @Override
    public Set<RichiestaNoleggio> getRichiesteNoleggio(){
        if(!richiesteNoleggioCaricate){
            richiesteNoleggioCaricate= true;
            super.setRichiesteNoleggio((Set<RichiestaNoleggio>) richiestaNoleggioDAO);
        }
        return super.getRichiesteNoleggio();
    }
    @Override
    public List<RichiestaManutenzione> getRichiesteManutenzione(){
        if(!richiesteManutenzioniCaricate){
            richiesteManutenzioniCaricate= true;
            List<RichiestaManutenzioneProxy> richiesta =richiesteManutenzioneDAO.getRichiesteManutenzioneInCorsoAzienda(super.getIdUtente());
            super.setRichiesteManutenzione(new ArrayList<>(richiesta));
        }
        return super.getRichiesteManutenzione();
    }
    @Override
    public List<Dipendente> getDipendenti(){
        if(!dipendentiCaricati){
            dipendentiCaricati= true;
            List<DipendenteProxy> dipendenti = richiestaAffiliazioneDAO.getDipendentiAzienda(super.getIdAziendaGestita());
            super.setDipendenti(new ArrayList<>(dipendenti));
        }
        return super.getDipendenti();
    }
    @Override
    public List<RichiestaAffiliazioneAzienda> getRichiesteAffiliazione(){
        if(!richiesteAffiliazioneCaricate){
            richiesteAffiliazioneCaricate= true;
            List<RichiestaAffiliazioneAziendaProxy> richieste = richiestaAffiliazioneDAO.getRichiesteAffiliazioneDaValutare(super.getIdAziendaGestita());
            super.setRichiesteAffiliazione(new ArrayList<>(richieste));
        }
        return super.getRichiesteAffiliazione();
    }
    @Override
    public Set<Veicolo> getVeicoliInGestione(){
        if(!gestioneVeicoloCaricate){
            gestioneVeicoloCaricate= true;
            super.setVeicoliInGestione((Set<Veicolo>) gestioneVeicoloAziendaDAO);
        }
        return super.getVeicoliInGestione();
    }
    @Override
    public Set<Fattura> getFatture(){
        if(!fatturaCaricate){
            fatturaCaricate= true;
            super.setFatture((Set<Fattura>) fatturaDAO);
        }
        return super.getFatture();
    }
    @Override
    public Set<LuogoAzienda> getLuoghiDepositoRitiro(){
        if(!luogoCaricate){
            luogoCaricate= true;
            super.setLuoghiDepositoRitiro((Set<LuogoAzienda>) luogoAziendaDAO);
        }
        return super.getLuoghiDepositoRitiro();
    }
}
