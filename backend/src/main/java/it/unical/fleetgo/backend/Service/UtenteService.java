package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class UtenteService {

    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private DataSource dataSource;

    public Integer registraUtente(UtenteDTO utenteDTO) throws SQLException {

        try(Connection connection = dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);

            if(utenteDAO.esisteEmail(utenteDTO.getEmail())){
                throw new IllegalArgumentException("Email non valida");
            }

            try {

                connection.setAutoCommit(false);
                Integer idAggiunta = utenteDAO.inserisciUtente(utenteDTO);

                if (idAggiunta == null) {
                    connection.rollback();
                    throw new RuntimeException("Problema durante l'inserimento dell'utente");
                }

                String urlImmagine = null;
                if(utenteDTO instanceof DipendenteDTO) {
                    urlImmagine = ((DipendenteDTO) utenteDTO).getUrlImmagine();
                }

                String passwordCriptata = passwordEncoder.encode(utenteDTO.getPassword());

                if (!credenzialiDAO.creaCredenzialiUtente(idAggiunta, utenteDTO.getEmail(), passwordCriptata, urlImmagine)) {
                    connection.rollback();
                    throw new RuntimeException("Problema durante l'inserimento delle credenziali");
                }

                connection.commit();
                return idAggiunta;

            }catch (SQLException ex) {
                throw new RuntimeException(ex);
            }finally {

                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {}
            }
        }
    }

    public void eliminaUtente(Integer idUtente) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            utenteDAO.eliminaUtente(idUtente);
        }
    }

    public ModificaDatiUtenteDTO getDatiUtente(Integer idUtente) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            return utenteDAO.getDatiUtente(idUtente);
        }
    }

    public void invioCodice(String email) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);

            int codiceOTP = RandomUtils.nextInt(100000, 999999);
            emailService.inviaCodiceOtp(email, codiceOTP);
            credenzialiDAO.inserimentoDatiRecuperoPassword(email, codiceOTP);
        }
    }

    public void modificaPassword(String email, Integer codiceOTP, String nuovaPassword) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);
            String passwordCriptata = passwordEncoder.encode(nuovaPassword);
            credenzialiDAO.modificaPassword(codiceOTP, passwordCriptata, email);
        }
    }

    public DipendenteDTO getDipendente(Integer idUtente) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            Dipendente d = utenteDAO.getDipendenteDaId(idUtente);
            return new DipendenteDTO(d);
        }
    }

    public ContenitoreStatisticheNumeriche getStatisticheNumeriche() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            return utenteDAO.getStatisticheNumeriche();
        }
    }

    public CredenzialiUtente getCredenzialiUtentiByEmail(String email) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);
            return credenzialiDAO.getCredenzialiUtenteByEmail(email);
        }
    }
}