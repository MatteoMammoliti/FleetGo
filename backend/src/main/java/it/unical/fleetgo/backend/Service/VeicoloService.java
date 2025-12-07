package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.GestioneVeicoloAziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.GestioneVeicoloAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class VeicoloService {

    private final Connection connection = DBManager.getInstance().getConnection();
    private final VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
    private final GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO = new GestioneVeicoloAziendaDAO(connection);

    public void registraVeicolo(VeicoloDTO veicoloDTO) throws SQLException {
        if(!veicoloDAO.aggiungiVeicolo(veicoloDTO)) {
            throw new RuntimeException("Problema durante l'inserimento del veicolo");
        }
    }

    public void eliminaVeicolo(String targaVeicolo) {
        if(!veicoloDAO.eliminaVeicolo(targaVeicolo)) {
            throw new RuntimeException("Problema durante l'eliminazione del veicolo");
        }
    }

    public List<Veicolo> getListaVeicoli() {
        try {
            return veicoloDAO.getVeicoliDisponibiliInPiattaforma();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la recupero della lista dei veicoli");
        }
    }

    public Veicolo getInformazioniVeicolo(String targa){
        return veicoloDAO.getVeicoloDaTarga(targa);
    }

    public void modificaDati(VeicoloDTO veicoloDTO) throws SQLException {
        connection.setAutoCommit(false);
        try{
            if(veicoloDTO.getNomeAziendaAffiliata()!=null){
                this.inserisciNuovoVeicoloGestito(veicoloDTO);
            } else if (veicoloDTO.getStatusCondizioneVeicolo()!=null) {
                this.cambiaStatusVeicolo(veicoloDTO);
            }
            connection.commit();
        }catch (Exception e){
            connection.rollback();
            throw new RuntimeException(e);
        }finally {
            connection.setAutoCommit(true);
        }
    }

    private void inserisciNuovoVeicoloGestito(VeicoloDTO veicolo) throws SQLException {
        GestioneVeicoloAziendaDTO contenitore = new GestioneVeicoloAziendaDTO();
        contenitore.setIdVeicolo(veicolo.getIdVeicolo());
        contenitore.setIdAzienda(veicolo.getIdAziendaAffiliata());
        gestioneVeicoloAziendaDAO.inserisciNuovoVeicoloGestito(contenitore);
        veicoloDAO.cambiaStatusVeicolo("Noleggiato",veicolo.getIdVeicolo());

    }
    private void cambiaStatusVeicolo(VeicoloDTO veicolo){
        veicoloDAO.cambiaStatusVeicolo(veicolo.getStatusCondizioneVeicolo(), veicolo.getIdVeicolo());
    }
}