package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.DBManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class UtenteService {
    Connection con = DBManager.getInstance().getConnection();
    UtenteDAO utenteDAO = new UtenteDAO(con);
    CredenzialiDAO credenzialiDAO = new CredenzialiDAO(con);
    RichiestaAffiliazioneAziendaDAO affiliazioneAziendaDAO = new RichiestaAffiliazioneAziendaDAO(con);
    RichiestaNoleggioDAO richiestaNoleggioDAO = new RichiestaNoleggioDAO(con);
    RichiesteManutenzioneDAO richiesteManutenzioneDAO = new RichiesteManutenzioneDAO(con);

    public Integer registraUtente(UtenteDTO utenteDTO) {
        if(utenteDAO.esisteEmail(utenteDTO.getEmail())){
            throw new IllegalArgumentException("Email non valida");
        }

        try {
            con.setAutoCommit(false);
            Integer idAggiunta = utenteDAO.inserisciUtente(utenteDTO);


            if (idAggiunta == null) {
                con.rollback();
                throw new RuntimeException("Problema durante l'inserimento dell'utente");
            }

            String urlImmagine = null;
            if(utenteDTO instanceof DipendenteDTO) {
                urlImmagine = ((DipendenteDTO) utenteDTO).getUrlImmagine();
            }

            if (!credenzialiDAO.creaCredenzialiUtente(idAggiunta, utenteDTO.getEmail(), utenteDTO.getPassword(), urlImmagine)) {
                con.rollback();
                throw new RuntimeException("Problema durante l'inserimento delle credenziali");
            }
            con.commit();
            return idAggiunta;
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ignored) {}
        }
    }

    public Integer loginUtente(String email,String password) throws SQLException {
        return credenzialiDAO.confrontaCredenzialiUtente(email,password);
    }

    public String getRuolo(Integer idUtente){
        return utenteDAO.getRuoloDaId(idUtente);
    }

}
