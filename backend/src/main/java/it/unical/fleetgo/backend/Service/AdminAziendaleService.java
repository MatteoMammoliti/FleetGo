package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.GestioneVeicoloAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaAffiliazioneAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class AdminAziendaleService {

    Connection connection = DBManager.getInstance().getConnection();
    private final UtenteDAO utenteDAO = new UtenteDAO(connection);
    private final RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO = new RichiestaAffiliazioneAziendaDAO(connection);
    private final AziendaDAO aziendaDAO = new AziendaDAO(connection);
    private final GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO = new GestioneVeicoloAziendaDAO(connection);

    public void modificaDati(ModificaDatiUtenteDTO dati) throws SQLException {
        utenteDAO.modificaDatiUtente(dati);
    }

    public List<Dipendente> getDipendenti(Integer idAzienda) {
        return richiestaAffiliazioneAziendaDAO.getDipendentiAzienda(idAzienda);
    }

    public Integer getIdAziendaGestita(Integer idAdmin) {
        return aziendaDAO.getIdAziendaGestita(idAdmin);
    }

    public void rimuoviDipendente(Integer idUtente, Integer idAzienda) {
        richiestaAffiliazioneAziendaDAO.rimuoviRichiestaAffiliazioneAzienda(idUtente, idAzienda);
    }

    public ContenitoreStatisticheNumeriche getStatoVeicolo(Integer idAzienda) {
        return gestioneVeicoloAziendaDAO.getStatoVeicoli(idAzienda);
    }
}