package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.DBManager;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired private EmailService emailService;

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

    public void eliminaUtente(Integer idUtente) {
        utenteDAO.eliminaUtente(idUtente);
    }

    public ModificaDatiUtenteDTO getDatiUtente(Integer idUtente){
        return utenteDAO.getDatiUtente(idUtente);
    }

    public void invioCodice(String email) {
        int codiceOTP = RandomUtils.nextInt(100000, 999999);

        emailService.inviaCodiceOtp(email, codiceOTP);
        credenzialiDAO.inserimentoDatiRecuperoPassword(email, codiceOTP);
    }

    public void modificaPassword(String email, Integer codiceOTP, String nuovaPassword) {
        credenzialiDAO.modificaPassword(codiceOTP, nuovaPassword, email);
    }

    public Dipendente getDipendente(Integer idUtente){
        return utenteDAO.getDipendenteDaId(idUtente);
    }

    public ContenitoreStatisticheNumeriche getStatisticheNumeriche(){
        return utenteDAO.getStatisticheNumeriche();
    }

    public CredenzialiUtente getCredenzialiUtentiByEmail(String email){
        return credenzialiDAO.getCredenzialiUtenteByEmail(email);
    }
}
