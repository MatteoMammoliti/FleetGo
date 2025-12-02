package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class ServiceAdminAziendale {
    Connection con = DBManager.getInstance().getConnection();
    private UtenteDAO utenteDAO = new UtenteDAO(con);


    public boolean modificaDati(String nome,String cognome,String data,String email,String nomeAzienda,String sedeAzienda,String pIva,Integer idUtente) throws SQLException {
        return utenteDAO.modificaDatiUtente(nome,cognome,data,email,nomeAzienda,sedeAzienda,pIva,idUtente);
    }
}
