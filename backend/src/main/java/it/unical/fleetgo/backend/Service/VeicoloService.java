package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Exceptions.ModelloAssegnatoAVeicoli;
import it.unical.fleetgo.backend.Exceptions.NoleggiInCorsoDissociamentoVeicolo;
import it.unical.fleetgo.backend.Models.DTO.ModelloDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.Modello;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VeicoloService {

    @Autowired private DataSource dataSource;
    @Autowired SalvataggioImmagineService salvataggioImmagineService;

    public void registraVeicolo(VeicoloDTO veicoloDTO) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            veicoloDAO.aggiungiVeicolo(veicoloDTO);
        }
    }

    public void eliminaVeicolo(String targaVeicolo) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            veicoloDAO.eliminaVeicolo(targaVeicolo);
        }
    }

    public List<VeicoloDTO> getListaVeicoli() throws SQLException, RuntimeException {
        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            List<Veicolo> listaVeicoli =  veicoloDAO.getVeicoliDisponibiliInPiattaforma();
            return listaVeicoli.stream().map(v -> new VeicoloDTO(v, true)).toList();
        }
    }

    public List<VeicoloDTO> getListaVeicoliAziendali(Integer azienda) throws SQLException, RuntimeException {
        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            List<Veicolo> listaVeicoli =  veicoloDAO.getVeicoliAssegnatiAzienda(azienda);
            List<VeicoloDTO> listaVeicoliDTO = new ArrayList<>();

            for(Veicolo v : listaVeicoli) {
                VeicoloDTO veicoloDTO = new VeicoloDTO(v,false);
                listaVeicoliDTO.add(veicoloDTO);
            }
            return listaVeicoliDTO;
        }
    }

    public VeicoloDTO getInformazioniVeicolo(String targa) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            Veicolo veicolo = veicoloDAO.getVeicoloByTarga(targa);
            return new VeicoloDTO(veicolo, true);
        }
    }

    public void associaVeicolo(VeicoloDTO veicoloDTO) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO = new GestioneVeicoloAziendaDAO(connection);
            gestioneVeicoloAziendaDAO.inserisciNuovoVeicoloGestito(veicoloDTO.getIdVeicolo(),  veicoloDTO.getIdAziendaAffiliata());
        }
    }

    public void disassociaVeicolo(VeicoloDTO veicoloDTO) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()){
            try{

                connection.setAutoCommit(false);

                RichiestaNoleggioDAO richiestaNoleggioDAO =  new RichiestaNoleggioDAO(connection);
                if(richiestaNoleggioDAO.getRichiesteAccettateEInCorsoPerVeicolo(veicoloDTO.getIdAziendaAffiliata(), veicoloDTO.getIdVeicolo())){
                    connection.rollback();
                    throw new NoleggiInCorsoDissociamentoVeicolo("Sono in corso noleggi per questo veicolo");
                };

                richiestaNoleggioDAO.eliminaRichiesteInAttesaPerVeicolo(veicoloDTO.getIdAziendaAffiliata(),  veicoloDTO.getIdVeicolo());

                GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO =  new GestioneVeicoloAziendaDAO(connection);
                if(!gestioneVeicoloAziendaDAO.eliminaVeicoloGestito(veicoloDTO.getIdVeicolo())){
                    connection.rollback();
                };

                VeicoloDAO veicoloDAO =  new VeicoloDAO(connection);
                if(!veicoloDAO.cambiaStatusContrattualeVeicolo("Disponibile", veicoloDTO.getIdVeicolo())){
                    connection.rollback();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Errore durante le operazioni nel DB");
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public void aggiuntaModello(ModelloDTO modello, MultipartFile immagine) throws SQLException, IOException {

        try(Connection conn = this.dataSource.getConnection()){
            try {
                conn.setAutoCommit(false);

                String urlImmagine = this.salvataggioImmagineService.salvaImmagine(immagine, "immagini-patenti");
                modello.setUrlImmagine(urlImmagine);

                ModelloDAO modelloDAO = new ModelloDAO(conn);
                modelloDAO.inserisciModello(modello);

            } catch (IOException e) {
                conn.rollback();
                throw new IOException(e);
            } catch (SQLException e) {
                conn.rollback();
                throw new  RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<ModelloDTO> getModelli() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            ModelloDAO modelloDAO = new ModelloDAO(connection);
            List<Modello> modelli = modelloDAO.getModelli();
            List<ModelloDTO> listaModelloDTO = new ArrayList<>();

            for(Modello modello : modelli) {
                listaModelloDTO.add(new ModelloDTO(modello));
            }
            return listaModelloDTO;
        }
    }

    public void eliminaModello(Integer idModello) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()){
            ModelloDAO modelloDAO = new ModelloDAO(connection);
            if(!modelloDAO.eliminaModello(idModello)){
                throw new ModelloAssegnatoAVeicoli();
            };
        }
    }

    public void impostaLuogoVeicolo(VeicoloDTO veicolo) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()){
            GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO = new GestioneVeicoloAziendaDAO(connection);
            gestioneVeicoloAziendaDAO.impostaLuogoVeicolo(veicolo);
        }
    }

    public RichiestaManutenzioneDTO getManutenzioneVeicolo(Integer idVeicolo, Integer idAdminAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiesteManutenzioneDAO richiesteManutenzioneDAO = new  RichiesteManutenzioneDAO(connection);
            return new RichiestaManutenzioneDTO(
                    richiesteManutenzioneDAO.getRichiestaManutenzioneByIdVeicolo(idVeicolo, idAdminAzienda),
                    false
            );
        }
    }

    public boolean eliminaRichiestaManutenzione(RichiestaManutenzioneDTO richiestaManutenzione) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiesteManutenzioneDAO richiesteManutenzioneDAO = new RichiesteManutenzioneDAO(connection);
            return richiesteManutenzioneDAO.rimuoviRichiestaManutenzione(
                    richiestaManutenzione.getIdManutenzione(),
                    richiestaManutenzione.getIdVeicolo()
            );
        }
    }
}