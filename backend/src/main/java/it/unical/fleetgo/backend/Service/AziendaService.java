package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.GestioneVeicoloAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AziendaService {

    @Autowired private DataSource dataSource;

    public void registraAzienda(AziendaDTO azienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            if(!aziendaDAO.inserisciAzienda(azienda)) {
                throw new RuntimeException("Problema durante l'inserimento dell'azienda");
            }
        }
    }

    public void eliminaAzienda(Integer idAdminAzienda) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            GestioneVeicoloAziendaDAO gestioneDAO = new GestioneVeicoloAziendaDAO(connection);

            connection.setAutoCommit(false);
            try{
                gestioneDAO.contrassegnaVeicoliLiberiPreEliminazioneAzienda(idAdminAzienda);
                aziendaDAO.eliminaAzienda(idAdminAzienda);
                utenteDAO.eliminaUtente(idAdminAzienda);
                connection.commit();
            }catch (Exception e){
                throw new RuntimeException(e);
            }finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<AziendaDTO> getElencoAziende() throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            List<Azienda> elencoAziende = aziendaDAO.getAziendeInPiattaforme();

            List<AziendaDTO> listaAziende = new ArrayList<>();
            for(Azienda a : elencoAziende) {
                AziendaDTO aziendaDTO = new AziendaDTO();
                aziendaDTO.setIdAzienda(a.getIdAzienda());
                aziendaDTO.setNomeAzienda(a.getNomeAzienda());
                aziendaDTO.setSedeAzienda(a.getSedeAzienda());
                aziendaDTO.setPIva(a.getPIva());
                aziendaDTO.setIdAdminAzienda(a.getIdAdmin());
                listaAziende.add(aziendaDTO);
            }
            return listaAziende;
        }
    }
}
