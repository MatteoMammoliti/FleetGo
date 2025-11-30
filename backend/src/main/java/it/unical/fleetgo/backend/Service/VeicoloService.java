package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class VeicoloService {
    Connection con = DBManager.getInstance().getConnection();
    private VeicoloDAO veicoloDAO = new VeicoloDAO(con);


    public boolean registraNuovoVeicolo(VeicoloDTO veicoloDTO) throws SQLException{
        return veicoloDAO.aggiungiVeicolo(veicoloDTO);
    }
}
