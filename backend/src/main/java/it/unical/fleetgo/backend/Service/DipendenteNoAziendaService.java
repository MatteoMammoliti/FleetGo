package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiAzienda;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaAffiliazioneAziendaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class DipendenteNoAziendaService {
    @Autowired DataSource dataSource;

    public List<ContenitoreDatiAzienda> getInfoAziende() throws SQLException{
        try(Connection connection = this.dataSource.getConnection()){
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            return aziendaDAO.getInformazioniAziendeInPiattaforme();
        }
    }

    public ContenitoreDatiAzienda getRichiestaInAttesa(Integer idDipendente) throws SQLException{
        try(Connection connection = this.dataSource.getConnection()){
            RichiestaAffiliazioneAziendaDAO dao = new RichiestaAffiliazioneAziendaDAO(connection);
            return dao.getRichiestaInAttesaDipendente(idDipendente);
        }
    }

    public boolean eliminaRichiestaInAttesa(Integer idDipendente,Integer idAzienda) throws SQLException{
        try(Connection connection = this.dataSource.getConnection()){
            RichiestaAffiliazioneAziendaDAO dao = new RichiestaAffiliazioneAziendaDAO(connection);
            return dao.rimuoviRichiestaAffiliazioneAzienda(idDipendente,idAzienda,false);
        }
    }

    public boolean aggiungiRichiestaAffiliazione(Integer idDipendente,Integer idAzienda) throws SQLException{
        try(Connection connection = this.dataSource.getConnection()){
            RichiestaAffiliazioneAziendaDAO dao = new RichiestaAffiliazioneAziendaDAO(connection);
            return dao.aggiungiRichiestaAffiliazioneAzienda(idDipendente,idAzienda);
        }
    }
}
