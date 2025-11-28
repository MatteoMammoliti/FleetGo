package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.DBManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
public class UtenteService {
    UtenteDAO utenteDAO = new UtenteDAO(DBManager.getInstance().getConnection());
    CredenzialiDAO credenzialiDAO = new CredenzialiDAO(DBManager.getInstance().getConnection());
    RichiestaAffiliazioneAziendaDAO affiliazioneAziendaDAO = new RichiestaAffiliazioneAziendaDAO(DBManager.getInstance().getConnection());
    RichiestaNoleggioDAO richiestaNoleggioDAO = new RichiestaNoleggioDAO(DBManager.getInstance().getConnection());
    RichiesteManutenzioneDAO richiesteManutenzioneDAO = new RichiesteManutenzioneDAO(DBManager.getInstance().getConnection());

    @Transactional(rollbackFor =  Exception.class)
    public void registraUtente(UtenteDTO utenteDTO) {
        if(!utenteDAO.esisteEmail(utenteDTO.getEmail())){
            throw new IllegalArgumentException("Email non valida");
        }
        Integer idAggiunta = utenteDAO.inserisciUtente(utenteDTO);
        if(idAggiunta==null){
            throw new RuntimeException("Problema durante l'inserimento dell'utente");
        }
        if(!credenzialiDAO.creaCredenzialiUtente(idAggiunta,utenteDTO.getEmail(),utenteDTO.getPassword(),((DipendenteDTO) utenteDTO).getUrlImmagine())){
            throw new RuntimeException("Problema durante l'inserimento delle credenziali");
        }

    }

    public Integer loginUtente(String email,String password) throws SQLException {
        return credenzialiDAO.confrontaCredenzialiUtente(email,password);
    }

    public String getRuolo(Integer idUtente){
        return utenteDAO.getRuoloDaId(idUtente);
    }

}
