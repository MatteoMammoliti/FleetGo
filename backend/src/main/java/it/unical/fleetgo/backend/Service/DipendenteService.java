package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Exceptions.DissociazioneAziendaNonConsentita;
import it.unical.fleetgo.backend.Models.DTO.*;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DipendenteService {

    @Autowired private DataSource dataSource;
    @Autowired private EmailService emailService;
    @Autowired private SalvataggioImmagineService salvataggioImmagineService;

    public String getNomeDipendente(Integer idDipendente) throws SQLException{
        try(Connection connection = dataSource.getConnection()){
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            Dipendente utente = utenteDAO.getDipendenteDaId(idDipendente);
            return utente.getNomeUtente();
        }
    }

    public ModificaDatiUtenteDTO getDatiUtente(Integer idDipendente) throws SQLException{
        try(Connection connection = dataSource.getConnection()){
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            return utenteDAO.getDatiDipendente(idDipendente);
        }
    }

    public String getImmaginePatente(Integer idDipendente) throws SQLException{
        try(Connection connection = dataSource.getConnection()){
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);
            return credenzialiDAO.getUrlPatente(idDipendente);
        }
    }

    public void aggiornaUrlPatente(Integer idDipendente, MultipartFile patente) throws SQLException, IOException {

        Connection connection = dataSource.getConnection();

        try {
            connection.setAutoCommit(false);

            String url = salvataggioImmagineService.salvaImmagine(patente, "immagini-patenti");
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);
            credenzialiDAO.updateUrlPatente(idDipendente, url);

            connection.commit();
        } catch (IOException e){
            connection.rollback();
            throw new IOException();
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public RichiestaNoleggioDTO getProssimoNoleggioDipendente(Integer idDipendente,Integer idAzienda) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            RichiestaNoleggio richiesta = dao.getProssimaRichiestaNoleggioDipendente(idDipendente,idAzienda);
            if(richiesta != null) {
                RichiestaNoleggioDTO dto = new RichiestaNoleggioDTO();
                dto.setDataRitiro(richiesta.getDataRitiro());
                dto.setOraInizio(richiesta.getOraInizio());
                dto.setOraFine(richiesta.getOraFine());
                dto.setMotivazione(richiesta.getMotivazione());
                dto.setStatoRichiesta(richiesta.getStatoRichiesta());
                Veicolo veicolo = richiesta.getVeicolo();
                VeicoloDTO veicoloDTO = new VeicoloDTO(veicolo,false);
                dto.setVeicolo(veicoloDTO);

                return dto;
            }
        }
        return null;
    }

    public StatisticheDipendenteDTO getStatisticheDipendente(Integer idDipendente,Integer idAzienda)throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            return dao.getStatisticheDipendente(idDipendente,idAzienda);
        }
    }

    public List<RichiestaNoleggioDTO> getRichiesteNoleggioDipendente(Integer idDipendente,Integer idAzienda) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            List<RichiestaNoleggio> richieste =dao.getRichiesteNoleggioDipendente(idDipendente,idAzienda);
            List<RichiestaNoleggioDTO> richiesteDTO = new ArrayList<>();
            for (RichiestaNoleggio ric :richieste) {
                RichiestaNoleggioDTO dto = new RichiestaNoleggioDTO(ric,false,false);
                Veicolo veicolo = ric.getVeicolo();
                VeicoloDTO veicoloDTO = new VeicoloDTO(veicolo,false);
                dto.setVeicolo(veicoloDTO);
                richiesteDTO.add(dto);
            }
            return richiesteDTO;
        }
    }

    public List<LuogoDTO> getLuoghiAziendaAssociata(Integer idAziendaAssociata) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            LuogoAziendaDAO dao = new LuogoAziendaDAO(connection);
            List<LuogoAzienda> luoghi=dao.getLuogiDisponibiliPerAzienda(idAziendaAssociata);
            List<LuogoDTO> luoghiDTO = new ArrayList<>();
            for (LuogoAzienda luogo : luoghi) {
                LuogoDTO dto= new LuogoDTO(luogo);
                luoghiDTO.add(dto);
            }
            return luoghiDTO;
        }
    }

    public void modificaDatiDipendente(ModificaDatiUtenteDTO dati) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            UtenteDAO dao = new UtenteDAO(connection);
            dao.modificaDatiUtente(dati);
        }
    }

    public void inviaSegnalazione(String messaggio, Integer idUtente, Integer idAzienda) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            CredenzialiDAO credenzialiDAO = new  CredenzialiDAO(connection);

            String emailMittente = credenzialiDAO.getCredenzialiUtenteById(idUtente).getEmail();
            String emailAdminDestinatrio = credenzialiDAO.getEmailAdminAziendale(idAzienda);

            emailService.inviaRichiestaAssistenza(
                    emailMittente,
                    emailAdminDestinatrio,
                    messaggio,
                    idUtente
            );
        }
    }

    public void lasciaAzienda(Integer idUtente, Integer idAzienda) throws SQLException {
        Connection connection = this.dataSource.getConnection();

        try {

            connection.setAutoCommit(false);

            RichiestaNoleggioDAO richiestaNoleggioDAO = new  RichiestaNoleggioDAO(connection);

            if(richiestaNoleggioDAO.controlloRichiestaInCorsoDipendente(idUtente)){
                connection.rollback();
                throw  new DissociazioneAziendaNonConsentita("Sono presenti noleggi in corso o accettati. Dissociazione non permessa");
            }

            richiestaNoleggioDAO.eliminaRichiesteNoleggioDipendenteEliminato(idUtente,idAzienda);

            RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO = new RichiestaAffiliazioneAziendaDAO(connection);
            richiestaAffiliazioneAziendaDAO.rimuoviRichiestaAffiliazioneAzienda(
                    idUtente,
                    idAzienda,
                    true
            );

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        } finally{
            connection.setAutoCommit(true);
        }
    }
}