package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import org.springframework.stereotype.Service;
import java.sql.Connection;

@Service
public class AziendaService {

    private final Connection connection = DBManager.getInstance().getConnection();
    private final AziendaDAO aziendaDAO = new AziendaDAO(connection);

    public void registraAzienda(AziendaDTO azienda) {
        if(!aziendaDAO.inserisciAzienda(azienda)) {
            throw new RuntimeException("Problema durante l'inserimento dell'azienda");
        }
    }

    public void eliminaAzienda(Integer idAzienda) {
        if(!aziendaDAO.eliminaAzienda(idAzienda)) {
            throw new RuntimeException("Problema durante l'eliminazione dell'azienda");
        }
    }
}
