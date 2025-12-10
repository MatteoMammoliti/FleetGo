package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.GestioneVeicoloAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class AziendaService {

    private final Connection connection = DBManager.getInstance().getConnection();
    private final AziendaDAO aziendaDAO = new AziendaDAO(connection);
    private final UtenteDAO utenteDAO = new UtenteDAO(connection);
    private final GestioneVeicoloAziendaDAO gestioneDAO = new GestioneVeicoloAziendaDAO(connection);

    public void registraAzienda(AziendaDTO azienda) {
        if(!aziendaDAO.inserisciAzienda(azienda)) {
            throw new RuntimeException("Problema durante l'inserimento dell'azienda");
        }
    }

    public void eliminaAzienda(Integer idAdminAzienda) throws SQLException {
        connection.setAutoCommit(false);
        try{
            gestioneDAO.contrassegnaVeicoliLiberiPreEliminazioneAzienda(idAdminAzienda);
            aziendaDAO.eliminaAzienda(idAdminAzienda);
            utenteDAO.eliminaUtente(idAdminAzienda);
            connection.commit();
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            connection.setAutoCommit(true);
        }

    }

    public List<Azienda> getElencoAziende() {
        return aziendaDAO.getAziendeInPiattaforme();
    }
}
