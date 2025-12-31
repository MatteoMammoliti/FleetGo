package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Exceptions.CodiceOTPErrato;
import it.unical.fleetgo.backend.Exceptions.EmailEsistente;
import it.unical.fleetgo.backend.Exceptions.EmailNonEsistente;
import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class UtenteService {

    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private DataSource dataSource;
    @Autowired private SalvataggioImmagineService salvataggioImmagineService;

    public void registraUtente(UtenteDTO utenteDTO, MultipartFile immagine) throws SQLException, IOException {

        try(Connection connection = dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);

            if(utenteDAO.esisteEmail(utenteDTO.getEmail())){
                throw new EmailEsistente();
            }

            try {

                connection.setAutoCommit(false);
                Integer idAggiunta = utenteDAO.inserisciUtente(utenteDTO);

                if (idAggiunta == null) {
                    connection.rollback();
                }

                String urlImg = salvataggioImmagineService.salvaImmagine(immagine, "immagini-patenti");
                ((DipendenteDTO) utenteDTO).setUrlImmagine(urlImg);

                String urlImmagine = ((DipendenteDTO) utenteDTO).getUrlImmagine();
                String passwordCriptata = passwordEncoder.encode(utenteDTO.getPassword());

                if (!credenzialiDAO.creaCredenzialiUtente(idAggiunta, utenteDTO.getEmail().toLowerCase(), passwordCriptata, urlImmagine)) {
                    connection.rollback();
                }

                connection.commit();

            }catch (SQLException ex) {
                connection.rollback();
                throw new RuntimeException(ex);
            } catch (IOException e) {
                connection.rollback();
                throw new IOException(e);
            } finally {

                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {}
            }
        }
    }

    public ModificaDatiUtenteDTO getDatiUtente(Integer idUtente) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            return utenteDAO.getDatiAdminAziendale(idUtente);
        }
    }

    public Integer getAziendaAssociataDipendente(Integer idUtente) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            return utenteDAO.getAziendaAssociataDipendente(idUtente);
        }
    }

    public void invioCodice(String email) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);
            UtenteDAO utenteDAO = new UtenteDAO(connection);

            if (!utenteDAO.esisteEmail(email)) {
                throw new EmailNonEsistente();
            }

            int codiceOTP = RandomUtils.nextInt(100000, 999999);
            emailService.inviaCodiceOtp(email, codiceOTP);
            credenzialiDAO.inserimentoDatiRecuperoPassword(email, codiceOTP);
        }
    }

    public void modificaPassword(String email, Integer codiceOTP, String nuovaPassword) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()){
            try{
                connection.setAutoCommit(false);

                CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);
                String passwordCriptata = passwordEncoder.encode(nuovaPassword);

                if(!credenzialiDAO.modificaPassword(codiceOTP, passwordCriptata, email)) {
                    connection.rollback();
                    throw new CodiceOTPErrato();
                }

                if(credenzialiDAO.getRuoloByEmail(email).equals("AdminAziendale")) {
                    if(!credenzialiDAO.impostaPrimoAccesso(email)){
                        connection.rollback();
                    }
                }

                connection.commit();

            } catch (SQLException ex) {
                connection.rollback();
                throw new RuntimeException(ex);
            } finally {
                connection.setAutoCommit(true);
            }
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