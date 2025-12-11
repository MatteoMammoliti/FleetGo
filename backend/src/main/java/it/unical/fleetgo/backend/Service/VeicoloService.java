package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.GestioneVeicoloAziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.GestioneVeicoloAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
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
            List<VeicoloDTO> listaVeicoliDTO = new ArrayList<>();

            for(Veicolo v : listaVeicoli) {
                VeicoloDTO veicoloDTO = getVeicoloDTO(v);
                listaVeicoliDTO.add(veicoloDTO);
            }
            return listaVeicoliDTO;
        }
    }

    public VeicoloDTO getInformazioniVeicolo(String targa) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            Veicolo veicolo = veicoloDAO.getVeicoloByTarga(targa);
            VeicoloDTO veicoloDTO = getVeicoloDTO(veicolo);
            LuogoDTO luogo = new LuogoDTO();
            luogo.setNomeLuogo(veicolo.getLuogo().getNomeLuogo());
            luogo.setLatitudine(veicolo.getLuogo().getLatitudine());
            luogo.setLongitudine(veicolo.getLuogo().getLongitudine());
            veicoloDTO.setLuogoRitiroDeposito(luogo);
            return veicoloDTO;
        }
    }

    public void modificaDati(VeicoloDTO veicoloDTO) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {

            connection.setAutoCommit(false);
            try {
                if(veicoloDTO.getNomeAziendaAffiliata() != null) {
                    this.inserisciNuovoVeicoloGestito(veicoloDTO);
                } else if(veicoloDTO.getStatusCondizioneVeicolo() != null) {
                    this.cambiaStatusVeicolo(veicoloDTO);
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void inserisciNuovoVeicoloGestito(VeicoloDTO veicolo) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            GestioneVeicoloAziendaDAO gestioneVeicoloAziendaDAO = new GestioneVeicoloAziendaDAO(connection);
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);

            GestioneVeicoloAziendaDTO contenitore = new GestioneVeicoloAziendaDTO();
            contenitore.setIdVeicolo(veicolo.getIdVeicolo());
            contenitore.setIdAzienda(veicolo.getIdAziendaAffiliata());
            gestioneVeicoloAziendaDAO.inserisciNuovoVeicoloGestito(contenitore);
            veicoloDAO.cambiaStatusVeicolo("Noleggiato",veicolo.getIdVeicolo());
        }
    }
    private void cambiaStatusVeicolo(VeicoloDTO veicolo) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            VeicoloDAO veicoloDAO = new VeicoloDAO(connection);
            veicoloDAO.cambiaStatusVeicolo(veicolo.getStatusCondizioneVeicolo(), veicolo.getIdVeicolo());
        }
    }

    private VeicoloDTO getVeicoloDTO(Veicolo v) {
        VeicoloDTO veicoloDTO = new VeicoloDTO();
        veicoloDTO.setIdVeicolo(v.getIdVeicolo());
        veicoloDTO.setTargaVeicolo(v.getTargaVeicolo());
        veicoloDTO.setUrlImmagine(v.getUrlImmagine());
        veicoloDTO.setModello(v.getModello());
        veicoloDTO.setTipoDistribuzioneVeicolo(v.getTipoDistribuzioneVeicolo());
        veicoloDTO.setLivelloCarburante(v.getLivelloCarburante());
        veicoloDTO.setStatusCondizioneVeicolo(v.getStatusCondizioneVeicolo());
        if(v.getNomeAziendaAffiliata()!=null){
            veicoloDTO.setNomeAziendaAffiliata(v.getNomeAziendaAffiliata());
            veicoloDTO.setIdAziendaAffiliata(v.getIdAziendaAffiliata());
        }else {
            veicoloDTO.setNomeAziendaAffiliata(null);
            veicoloDTO.setIdAziendaAffiliata(null);
        }
        return veicoloDTO;
    }
}