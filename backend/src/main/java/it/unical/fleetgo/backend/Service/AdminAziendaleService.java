package it.unical.fleetgo.backend.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import it.unical.fleetgo.backend.Models.DTO.*;
import it.unical.fleetgo.backend.Models.DTO.Utente.AdminAziendaleDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.*;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired private GeneratorePdfService generatorePdfService;

    @Value("${stripe.api.key}") private String stripeApiKey;
    @Value("${app.frontend.url}") private String urlReinderizzazione;

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

    public boolean rimuoviDipendente(Integer idUtente, Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try{
                RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO =
                        new RichiestaAffiliazioneAziendaDAO(connection);
                RichiestaNoleggioDAO daoNoleggio=new RichiestaNoleggioDAO(connection);
                daoNoleggio.aggiornaStatiNoleggi();
                if(daoNoleggio.controlloRichiestaInCorsoDipendente(idUtente)){
                    connection.rollback();
                    return false;
                }
                daoNoleggio.eliminaRichiesteNoleggioDipendenteEliminato(idUtente,idAzienda);
                richiestaAffiliazioneAziendaDAO.rimuoviRichiestaAffiliazioneAzienda(idUtente, idAzienda);
                connection.commit();
                return true;
            }catch(SQLException e){
                connection.rollback();
                e.printStackTrace();
                throw new SQLException(e);
            }finally{
                connection.setAutoCommit(true);
            }
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
            return richiestaNoleggioDAO.getNumRichiesteNoleggioDaAccettare(idAzienda);
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

    public Integer getNumeroFattureDaPagare(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            return fatturaDAO.getNmeroFattureDaPagareByIdAzienda(idAzienda);
        }
    }

    public List<FatturaDTO> getFattureEmesse(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            List<Fattura> fattureEmesse = fatturaDAO.getFattureEmesseAdAzienda(idAzienda);
            List<FatturaDTO> fattureEmesseDTO = new ArrayList<>();

            for(Fattura f : fattureEmesse) {
                fattureEmesseDTO.add(new FatturaDTO(f, false, f.getIdOffertaApplicata() != null));
            }
            return fattureEmesseDTO;
        }
    }

    public byte[] downloadFattura(Integer numeroFattura) throws SQLException{
        try(Connection connection = this.dataSource.getConnection()) {
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            Fattura f = fatturaDAO.getFatturaByNumeroFattura(numeroFattura);

            if(f != null) {
                return this.generatorePdfService.generaPdfFattura(
                        new FatturaDTO(f, true, f.getIdOffertaApplicata() != null)
                );
            }
        }
        return null;
    }

    public List<Integer> getAnniFatture(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            return fatturaDAO.getAnniFatturePerAzienda(idAzienda);
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

    public List<RichiestaNoleggioDTO> getRichiesteNoleggio(Integer idDipendente) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaNoleggioDAO richiestaNoleggioDAO = new RichiestaNoleggioDAO(connection);
            List<RichiestaNoleggioDTO> richiesteNoleggio = new ArrayList<>();
            richiestaNoleggioDAO.aggiornaStatiNoleggi();
            List<RichiestaNoleggio> richieste = richiestaNoleggioDAO.getRichiesteNoleggioAccettateByIdDipendente(idDipendente);

            for(RichiestaNoleggio richiesta: richieste) {
                richiesteNoleggio.add(new RichiestaNoleggioDTO(richiesta, false, false));
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

    public List<RichiestaNoleggioDTO> getPrenotazioni(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaNoleggioDAO richiestaNoleggioDAO = new RichiestaNoleggioDAO(connection);

            List<RichiestaNoleggio> richieste = richiestaNoleggioDAO.getRichiesteNoleggioAccettateByIdAzienda(idAzienda);
            List<RichiestaNoleggioDTO> richiesteDTO = new ArrayList<>();

            for(RichiestaNoleggio richiesta : richieste) {
                richiesteDTO.add(new RichiestaNoleggioDTO(richiesta, false, false));
            }
            return richiesteDTO;
        }
    }

    public RichiestaNoleggioDTO getRichiestaNoleggio(Integer idRichiesta) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaNoleggioDAO richiestaNoleggioDAO = new  RichiestaNoleggioDAO(connection);
            RichiestaNoleggio r = richiestaNoleggioDAO.getRichiestaNoleggioById(idRichiesta);
            return new RichiestaNoleggioDTO(r, true, true);
        }
    }

    public List<RichiestaNoleggioDTO> getRichiesteDaAccettare(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaNoleggioDAO richiestaNoleggioDAO = new  RichiestaNoleggioDAO(connection);
            List<RichiestaNoleggioDTO> richiesteDTO = new ArrayList<>();
            List<RichiestaNoleggio> richieste = richiestaNoleggioDAO.getRichiesteDaAccettare(idAzienda);

            for(RichiestaNoleggio r : richieste) {
                richiesteDTO.add(new RichiestaNoleggioDTO(r, true, true));
            }
            return richiesteDTO;
        }
    }

    public String pagaFattura(Integer numeroFattura) throws SQLException, StripeException {
        Stripe.apiKey = this.stripeApiKey;

        try(Connection connection = this.dataSource.getConnection()) {
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            FatturaDTO fattura = new FatturaDTO(
                    fatturaDAO.getFatturaByNumeroFattura(numeroFattura),
                    false,
                    false
            );

            String titoloPagamento = "Pagamento Fattura FleetGo #" + numeroFattura;
            long importoFatturaInCentesimi = (long) (fattura.getCosto() * 100);

            SessionCreateParams.LineItem.PriceData.ProductData prodotto = SessionCreateParams.LineItem.PriceData.ProductData.builder().
                    setName(titoloPagamento).
                    build();

            SessionCreateParams.LineItem.PriceData bodyPaginaPagamento = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("eur")
                    .setUnitAmount(importoFatturaInCentesimi)
                    .setProductData(prodotto).build();

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl(this.urlReinderizzazione + "?success=true&fattura=" + numeroFattura)
                            .setCancelUrl(this.urlReinderizzazione + "?canceled=true")
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(bodyPaginaPagamento).build()
                            ).build();

            Session session = Session.create(params);
            return session.getUrl();

        }
    }

    public boolean contrassegnaFatturaPagata(Integer numeroFattura) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            return fatturaDAO.pagaFattura(numeroFattura);
        }
    }

    public boolean approvaRichiestaNoleggio(Integer idRichiesta) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaNoleggioDAO richiestaNoleggioDAO = new  RichiestaNoleggioDAO(connection);
            return richiestaNoleggioDAO.accettaRichiestaNoleggio(idRichiesta);
        }
    }

    public boolean rifiutaRichiesta(Integer idRichiesta) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaNoleggioDAO richiestaNoleggioDAO = new  RichiestaNoleggioDAO(connection);
            return richiestaNoleggioDAO.rifiutaRichiestaNoleggio(idRichiesta);
        }
    }

    public void approvazioneConRifiutoAutomatico(Integer idDaApprovare, List<Integer> idRichiesteDaRifiutare) throws SQLException {

        Connection connection = this.dataSource.getConnection();

        try{

            connection.setAutoCommit(false);

            RichiestaNoleggioDAO richiestaNoleggioDAO = new   RichiestaNoleggioDAO(connection);
            boolean approvata = richiestaNoleggioDAO.accettaRichiestaNoleggio(idDaApprovare);

            if(!approvata) {
                connection.rollback();
            }

            for(Integer id: idRichiesteDaRifiutare) {
                richiestaNoleggioDAO.rifiutaRichiestaNoleggio(id);
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }
}