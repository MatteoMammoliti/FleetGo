package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class AdminAziendaleService {
    Connection con = DBManager.getInstance().getConnection();
    private final UtenteDAO utenteDAO = new UtenteDAO(con);

    public void modificaDati(ModificaDatiUtenteDTO dati) throws SQLException {
        utenteDAO.modificaDatiUtente(dati);
    }
}
