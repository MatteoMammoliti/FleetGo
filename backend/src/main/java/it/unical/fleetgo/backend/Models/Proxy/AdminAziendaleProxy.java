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
            super.setIdAziendaGestita(aziendaDAO.getIdAziendaGestita(
                    this.getIdUtente()
            ));
        }
        return super.getIdAziendaGestita();
    }

    @Override
    public List<RichiestaNoleggio> getRichiesteNoleggio(){
        if(!richiesteNoleggioCaricate){
            richiesteNoleggioCaricate= true;
            List<RichiestaNoleggio> richieste = richiestaNoleggioDAO.getRichiesteNoleggioAziendaDaAccettare(super.getIdUtente());
            super.setRichiesteNoleggio(new ArrayList<>(richieste));
        }
        return super.getRichiesteNoleggio();
    }

    @Override
    public List<RichiestaManutenzione> getRichiesteManutenzione(){
        if(!richiesteManutenzioniCaricate){
            richiesteManutenzioniCaricate= true;
            List<RichiestaManutenzione> richiesta =richiesteManutenzioneDAO.getRichiesteManutenzioneInCorsoAzienda(super.getIdUtente());
            super.setRichiesteManutenzione(new ArrayList<>(richiesta));
        }
        return super.getRichiesteManutenzione();
    }

    @Override
    public List<Dipendente> getDipendenti(){
        if(!dipendentiCaricati){
            dipendentiCaricati= true;
            List<Dipendente> dipendenti = richiestaAffiliazioneDAO.getDipendentiAzienda(super.getIdAziendaGestita());
            super.setDipendenti(new ArrayList<>(dipendenti));
        }
        return super.getDipendenti();
    }

    @Override
    public List<RichiestaAffiliazioneAzienda> getRichiesteAffiliazione(){
        if(!richiesteAffiliazioneCaricate){
            richiesteAffiliazioneCaricate= true;
            List<RichiestaAffiliazioneAzienda> richieste = richiestaAffiliazioneDAO.getRichiesteAffiliazioneDaValutare(
                    super.getIdAziendaGestita()
            );
            super.setRichiesteAffiliazione(new ArrayList<>(richieste));
        }
        return super.getRichiesteAffiliazione();
    }

    @Override
    public Set<GestioneVeicoloAzienda> getVeicoliInGestione(){
        if(!gestioneVeicoloCaricate){
            gestioneVeicoloCaricate= true;
            super.setVeicoliInGestione(
                    gestioneVeicoloAziendaDAO.getVeicoliInGestioneAzienda(super.getIdAziendaGestita())
            );
        }
        return super.getVeicoliInGestione();
    }

    @Override
    public Set<Fattura> getFatture(){
        if(!fatturaCaricate){
            fatturaCaricate= true;
            super.setFatture(fatturaDAO.getFattureEmesseAdAzienda(
                    super.getIdAziendaGestita()
            ));
        }
        return super.getFatture();
    }

    @Override
    public Set<LuogoAzienda> getLuoghiDepositoRitiro(){
        if(!luogoCaricate){
            luogoCaricate= true;
            super.setLuoghiDepositoRitiro(luogoAziendaDAO.getLuogiDisponibiliPerAzienda(
                    super.getIdAziendaGestita()
            ));
        }
        return super.getLuoghiDepositoRitiro();
    }
}