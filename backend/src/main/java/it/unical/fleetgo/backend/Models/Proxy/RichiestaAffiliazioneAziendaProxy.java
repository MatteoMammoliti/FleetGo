package it.unical.fleetgo.backend.Models.Proxy;
import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaAffiliazioneAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;

public class RichiestaAffiliazioneAziendaProxy extends RichiestaAffiliazioneAzienda {

    private final UtenteDAO utenteDAO;
    private boolean utenteCaricato = false;

    public RichiestaAffiliazioneAziendaProxy(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    @Override
    public Dipendente getUtente() {
        if(!utenteCaricato){
            Dipendente u = utenteDAO.getDipendenteById(super.getIdUtente());
            utenteCaricato = true;
            super.setUtente(u);
        }
        return super.getUtente();
    }
}