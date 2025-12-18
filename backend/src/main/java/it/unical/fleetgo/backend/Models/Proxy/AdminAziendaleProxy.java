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
    private final CredenzialiDAO credenzialiDAO;
    private boolean emailCaricata = false;
    private boolean aziendaCaricata = false;
    private boolean idAziendaGestitaCaricato = false;
    private boolean richiesteNoleggioCaricate = false;
    private boolean richiesteManutenzioniCaricate = false;
    private boolean dipendentiCaricati = false;
    private boolean gestioneVeicoloCaricate= false;
    private boolean fatturaCaricate= false;
    private boolean luogoCaricate= false;
    private boolean richiesteAffiliazioneCaricate= false;

    public AdminAziendaleProxy(AziendaDAO aziendaDAO, RichiestaNoleggioDAO richiestaNoleggioDAO,
                               RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO,
                               GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO,
                               FatturaDAO fatturaDAO,
                               LuogoAziendaDAO luogoAziendaDAO,
                               RichiesteManutenzioneDAO richiesteManutenzioneDAO, CredenzialiDAO credenzialiDAO) {

        this.aziendaDAO = aziendaDAO;
        this.richiestaNoleggioDAO = richiestaNoleggioDAO;
        this.richiestaAffiliazioneDAO = richiestaAffiliazioneAziendaDAO;
        this.fatturaDAO = fatturaDAO;
        this.gestioneVeicoloAziendaDAO = gestioneVeicoloAziendaDAO;
        this.luogoAziendaDAO = luogoAziendaDAO;
        this.richiesteManutenzioneDAO = richiesteManutenzioneDAO;
        this.credenzialiDAO = credenzialiDAO;
    }

    @Override
    public Integer getIdAziendaGestita(){
        if(!idAziendaGestitaCaricato){
            idAziendaGestitaCaricato = true;
            super.setIdAziendaGestita(aziendaDAO.getIdAziendaGestita(
                    this.getIdUtente()
            ));
        }
        return super.getIdAziendaGestita();
    }

    @Override
    public String getEmail() {
        if(!emailCaricata) {
            emailCaricata = true;
            super.setEmail(credenzialiDAO.getCredenzialiUtente(this.getIdUtente()).getEmail());
        }
        return super.getEmail();
    }

    @Override
    public Azienda getAziendaGestita(){
        if(!aziendaCaricata) {
            aziendaCaricata = true;
            super.setAziendaGestita(aziendaDAO.getAziendaById(this.getIdAziendaGestita()));
        }
        return super.getAziendaGestita();
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
    public List<GestioneVeicoloAzienda> getVeicoliInGestione(){
        if(!gestioneVeicoloCaricate){
            gestioneVeicoloCaricate= true;
            super.setVeicoliInGestione(
                    gestioneVeicoloAziendaDAO.getVeicoliInGestioneAzienda(super.getIdAziendaGestita())
            );
        }
        return super.getVeicoliInGestione();
    }

    @Override
    public List<Fattura> getFatture(){
        if(!fatturaCaricate){
            fatturaCaricate= true;
            super.setFatture(fatturaDAO.getFattureEmesseAdAzienda(
                    super.getIdAziendaGestita()
            ));
        }
        return super.getFatture();
    }

    @Override
    public List<LuogoAzienda> getLuoghiDepositoRitiro(){
        if(!luogoCaricate){
            luogoCaricate= true;
            super.setLuoghiDepositoRitiro(luogoAziendaDAO.getLuogiDisponibiliPerAzienda(
                    super.getIdAziendaGestita()
            ));
        }
        return super.getLuoghiDepositoRitiro();
    }
}