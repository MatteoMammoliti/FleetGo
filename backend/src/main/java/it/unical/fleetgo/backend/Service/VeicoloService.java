package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.ModelloDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.GestioneVeicoloAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.ModelloDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaNoleggioDAO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.Entity.Modello;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VeicoloService {

    @Autowired private DataSource dataSource;

    public void registraVeicolo(VeicoloDTO veicoloDTO) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            if(!veicoloDAO.aggiungiVeicolo(veicoloDTO)) {
                throw new RuntimeException("Problema durante l'inserimento del veicolo");
            }
        }
    }

    public void eliminaVeicolo(String targaVeicolo) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            if(!veicoloDAO.eliminaVeicolo(targaVeicolo)) {
                throw new RuntimeException("Problema durante l'eliminazione del veicolo");
            }
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

    public void disassociaVeicolo(VeicoloDTO veicoloDTO) throws SQLException, IllegalStateException {

        Connection connection = this.dataSource.getConnection();

        try{

            connection.setAutoCommit(false);

            RichiestaNoleggioDAO richiestaNoleggioDAO =  new RichiestaNoleggioDAO(connection);
            if(richiestaNoleggioDAO.getRichiesteAccettateEInCorsoPerVeicolo(veicoloDTO.getIdAziendaAffiliata(), veicoloDTO.getIdVeicolo())){
                connection.rollback();
                System.out.println("sono qui");
                throw new IllegalStateException("Sono in corso noleggi per questo veicolo");
            };

            richiestaNoleggioDAO.eliminaRichiesteInAttesaPerVeicolo(veicoloDTO.getIdAziendaAffiliata(),  veicoloDTO.getIdVeicolo());

            GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO =  new GestioneVeicoloAziendaDAO(connection);
            if(!gestioneVeicoloAziendaDAO.eliminaVeicoloGestito(veicoloDTO.getIdVeicolo())){
                connection.rollback();
                throw new RuntimeException("Errore durante le operazioni nel DB");
            };

            if(!gestioneVeicoloAziendaDAO.cambiaStatusContrattuale(veicoloDTO.getIdVeicolo())){
                connection.rollback();
                throw new RuntimeException("Errore durante le operazioni nel DB");
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            throw new RuntimeException("Errore durante le operazioni nel DB");
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void cambiaStatusManutenzioneVeicolo(VeicoloDTO veicolo) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            veicoloDAO.cambiaStatusManutenzioneVeicolo(veicolo.getInManutenzione(), veicolo.getIdVeicolo());
        }
    }

    public boolean aggiuntaModello(ModelloDTO modello) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            ModelloDAO modelloDAO = new ModelloDAO(connection);
            return modelloDAO.inserisciModello(modello);
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

    public Boolean eliminaModello(Integer idModello) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()){
            ModelloDAO modelloDAO = new ModelloDAO(connection);
            return modelloDAO.eliminaModello(idModello);
        }
    }
}