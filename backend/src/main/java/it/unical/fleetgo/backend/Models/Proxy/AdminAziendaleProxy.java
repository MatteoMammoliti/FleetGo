package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.*;

import java.util.Set;

public class AdminAziendaleProxy extends AdminAziendale {
    private AziendaDAO aziendaDAO;
    private RichiestaNoleggioDAO richiestaNoleggioDAO;
    private RichiesteManutenzioneDAO richiesteManutenzioneDAO;
    private RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneDAO;
    private GestioneVeicoloAziendaDAO  gestioneVeicoloAziendaDAO;
    private FatturaDAO fatturaDAO;
    private LuogoAziendaDAO luogoAziendaDAO;

    private boolean aziendaCaricata= false;
    private boolean richiesteNoleggioCaricate = false;
    private boolean richiesteManutenzioniCaricate= false;
    private boolean dipendentiCaricati = false;
    private boolean gestioneVeicoloCaricate= false;
    private boolean fatturaCaricate= false;
    private boolean luogoCaricate= false;

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
            Integer idAzienda= aziendaDAO;
            aziendaCaricata= true;
            super.setIdAziendaGestita(idAzienda);
        }
        return super.getIdAziendaGestita();
    }
    @Override
    public Set<RichiestaNoleggio> getRichiesteNoleggio(){
        if(!richiesteNoleggioCaricate){
            Set<RichiestaNoleggio> richieste= richiestaNoleggioDAO;
            richiesteNoleggioCaricate= true;
            super.setRichiesteNoleggio(richieste);
        }
        return super.getRichiesteNoleggio();
    }
    @Override
    public Set<RichiestaManutenzione> getRichiesteNoleggio(){
        if(!richiesteManutenzioniCaricate){
            Set<RichiestaManutenzione> richieste = richiesteManutenzioneDAO;
            richiesteManutenzioniCaricate= true;
            super.setRichiesteManutenzione(richieste);
        }
        return super.getRichiesteManutenzione();
    }
    @Override
    public Set<Dipendente> getDipendenti(){
        if(!dipendentiCaricati){
            Set<Dipendente> dipendenti = richiestaAffiliazioneDAO;
            dipendentiCaricati= true;
            super.setDipendenti(dipendenti);
        }
        return super.getDipendenti();
    }
    @Override
    public Set<Veicolo> getVeicoliInGestione(){
        if(!gestioneVeicoloCaricate){
            Set<Veicolo> veicoli = gestioneVeicoloAziendaDAO;
            gestioneVeicoloCaricate= true;
            super.setVeicoliInGestione(veicoli);
        }
        return super.getVeicoliInGestione();
    }
    @Override
    public Set<Fattura> getFatture(){
        if(!fatturaCaricate){
            Set<Fattura> fatture = fatturaDAO;
            fatturaCaricate= true;
            super.setFatture(fatture);
        }
        return super.getFatture();
    }
    @Override
    public Set<LuogoAzienda> getLuoghiDepositoRitiro(){
        if(!luogoCaricate){
            Set<LuogoAzienda> luoghi = luogoAziendaDAO;
            luogoCaricate= true;
            super.setLuoghiDepositoRitiro(luoghi);
        }
        return super.getLuoghiDepositoRitiro();
    }


}
