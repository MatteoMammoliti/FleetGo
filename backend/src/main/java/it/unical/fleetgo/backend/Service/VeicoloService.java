package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
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

    public void registraVeicolo(VeicoloDTO veicoloDTO) throws SQLException {
        if(!veicoloDAO.aggiungiVeicolo(veicoloDTO)) {
            throw new RuntimeException("Problema durante l'inserimento del veicolo");
        }
    }

    public void eliminaVeicolo(VeicoloDTO veicoloDTO) {

        Veicolo veicolo = new Veicolo();
        veicolo.setTargaVeicolo(veicoloDTO.getTargaVeicolo());
        veicolo.setModello(veicoloDTO.getModello());
        veicolo.setTipoDistribuzioneVeicolo(veicoloDTO.getTipoDistribuzioneVeicolo());

        int idVeicolo = veicoloDAO.getIdVeicoloDaDettagli(veicolo);
        if(!veicoloDAO.eliminaVeicolo(idVeicolo)) {
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
}