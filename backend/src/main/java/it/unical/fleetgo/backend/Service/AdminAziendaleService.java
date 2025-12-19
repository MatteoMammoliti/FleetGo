package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.*;
import it.unical.fleetgo.backend.Models.DTO.Utente.AdminAziendaleDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Offerta;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaAffiliazioneAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminAziendaleService {

    @Autowired private DataSource dataSource;
    @Autowired private EmailService emailService;

    public void modificaDati(ModificaDatiUtenteDTO dati) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            utenteDAO.modificaDatiUtente(dati);
        }
    }

    public List<DipendenteDTO> getDipendenti(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO =
                    new RichiestaAffiliazioneAziendaDAO(connection);

            List<Dipendente> dipendenti = richiestaAffiliazioneAziendaDAO.getDipendentiAzienda(idAzienda);
            List<DipendenteDTO> listaDipendenti = new ArrayList<>();

            for(Dipendente d : dipendenti) {
                DipendenteDTO dipendenteDTO = new DipendenteDTO(d);
                dipendenteDTO.setEmail(d.getCredenziali().getEmail());
                listaDipendenti.add(dipendenteDTO);
            }

            return listaDipendenti;
        }
    }

    public Integer getIdAziendaGestita(Integer idAdmin) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            return aziendaDAO.getIdAziendaGestita(idAdmin);
        }
    }

    public void rimuoviDipendente(Integer idUtente, Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO =
                    new RichiestaAffiliazioneAziendaDAO(connection);
            richiestaAffiliazioneAziendaDAO.rimuoviRichiestaAffiliazioneAzienda(idUtente, idAzienda);
        }
    }

    public List<LuogoDTO> getLuoghiCorrenti(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            LuogoAziendaDAO luogoAziendaDAO = new LuogoAziendaDAO(connection);

            List<LuogoAzienda> l = luogoAziendaDAO.getLuogiDisponibiliPerAzienda(idAzienda);
            List<LuogoDTO> luoghi = new ArrayList<>();

            for(LuogoAzienda luogo : l) {
                luoghi.add(new LuogoDTO(luogo));
            }
            return luoghi;
        }
    }

    public void aggiungiLuogo(LuogoDTO luogo) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            LuogoAziendaDAO luogoAziendaDAO = new LuogoAziendaDAO(connection);
            luogoAziendaDAO.inserisciLuogo(luogo);
        }
    }

    public List<OffertaDTO> getOfferteAttive() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            OffertaDAO offertaDAO = new OffertaDAO(connection);
            List<Offerta> offerte = offertaDAO.getOfferteAttive();
            List<OffertaDTO> offerteDTO = new ArrayList<>();

            for(Offerta off : offerte) {
                offerteDTO.add(new OffertaDTO(off));
            }

            return offerteDTO;
        }
    }

    public Integer getNumRichiesteNoleggio(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaNoleggioDAO richiestaNoleggioDAO = new RichiestaNoleggioDAO(connection);
            return richiestaNoleggioDAO.getNumRichiesteNoleggio(idAzienda);
        }
    }

    public Integer getNumRichiesteAffiliazione(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaAffiliazioneAziendaDAO richiesteAffiliazioneAziendaDAO = new RichiestaAffiliazioneAziendaDAO(connection);
            return richiesteAffiliazioneAziendaDAO.getNumRichiesteAffiliazione(idAzienda);
        }
    }

    public Float getSpesaMensile(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            GeneraFatturaDAO generaFatturaDAO = new GeneraFatturaDAO(connection);
            Float totale = generaFatturaDAO.getSpesaMensileAzienda(idAzienda);
            return (totale != null) ? totale : (float) 0.0;
        }
    }

    public Integer getVeicoliNoleggiati(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO = new GestioneVeicoloAziendaDAO(connection);
            return gestioneVeicoloAziendaDAO.getVeicoliNoleggiatiByIdAzienda(idAzienda);
        }
    }

    public String getNomeAziendaGestita(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            return aziendaDAO.getNomeAziendaById(idAzienda);
        }
    }

    public String getNomeCognomeAdmin(Integer idUtente) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            return utenteDAO.getNomeCognomeAdminById(idUtente);
        }
    }

    public Integer getNumeroPatentiDaAccettare(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);
            return credenzialiDAO.getNumeroDipendentiConPatentiDaAccettare(idAzienda);
        }
    }

    public void richiediAppuntamento(Integer idUtente) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            AdminAziendaleDTO admin = new AdminAziendaleDTO(utenteDAO.getAdminAziendaDaId(idUtente), true);

            this.emailService.inviaMailRichiestaAppuntamento(
                    admin,
                    "fleetgo@fleetgo.com"
            );
        }
    }

    public void approvaPatente(Integer idUtente) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);
            credenzialiDAO.approvaPatente(idUtente);
        }
    }
    public List<RichiestaNoleggioDTO> getRichiesteNoleggio(Integer idDipendente) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaNoleggioDAO richiestaNoleggioDAO = new RichiestaNoleggioDAO(connection);
            List<RichiestaNoleggioDTO> richiesteNoleggio = new ArrayList<>();
            List<RichiestaNoleggio> richieste = richiestaNoleggioDAO.getRichiesteNoleggioAccettateByIdDipendente(idDipendente);

            for(RichiestaNoleggio richiesta: richieste) {
                richiesteNoleggio.add(new RichiestaNoleggioDTO(richiesta, false));
            }
            return richiesteNoleggio;
        }
    }

    public List<RichiestaAffiliazioneAziendaDTO> getRichiesteAffiliazioneDaAccettare(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO = new RichiestaAffiliazioneAziendaDAO(connection);

            List<RichiestaAffiliazioneAzienda> richieste = richiestaAffiliazioneAziendaDAO.getRichiesteAffiliazioneDaAccettare(idAzienda);
            List<RichiestaAffiliazioneAziendaDTO> richiesteDTO = new ArrayList<>();

            for(RichiestaAffiliazioneAzienda richiesta : richieste) {
                richiesteDTO.add(new RichiestaAffiliazioneAziendaDTO(richiesta, true));
            }
            return richiesteDTO;
        }
    }

    public void rispondiRichiestaAffiliazione(Integer idUtente, Integer idAzienda, boolean risposta) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO = new RichiestaAffiliazioneAziendaDAO(connection);
            richiestaAffiliazioneAziendaDAO.rispondiRichiestaAffiliazione(idAzienda, idUtente, risposta);
        }
    }
}