package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.util.List;

@Service
public class AziendaService {

    private final Connection connection = DBManager.getInstance().getConnection();
    private final AziendaDAO aziendaDAO = new AziendaDAO(connection);

    public void registraAzienda(AziendaDTO azienda) {
        if(!aziendaDAO.inserisciAzienda(azienda)) {
            throw new RuntimeException("Problema durante l'inserimento dell'azienda");
        }
    }

    public void eliminaAzienda(Integer idAdminAzienda) {
        if(!aziendaDAO.eliminaAzienda(idAdminAzienda)) {
            throw new RuntimeException("Problema durante l'eliminazione dell'azienda");
        }
    }

    public List<Azienda> getElencoAziende() {
        return aziendaDAO.getAziendeInPiattaforme();
    }
}
