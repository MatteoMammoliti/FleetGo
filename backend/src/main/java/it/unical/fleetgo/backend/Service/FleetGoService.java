package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.FatturaDaGenerareDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.GeneraFatturaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiesteManutenzioneDAO;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaManutenzione;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FleetGoService {
    @Autowired private DataSource dataSource;

    public List<FatturaDaGenerareDTO> getGeneraFattura() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()){
            GeneraFatturaDAO generaFatturaDAO = new GeneraFatturaDAO(connection);
            List<FatturaDaGenerareDTO> fatture = generaFatturaDAO.generaFatturaDaGenerare();
            return fatture;
        }
    }
    public List<RichiestaManutenzioneDTO> getRichiesteManutenzioneDaAccettare() throws SQLException {
        List<RichiestaManutenzioneDTO> richiesteDTO = new ArrayList<>();
        try(Connection connection=this.dataSource.getConnection()){
            RichiesteManutenzioneDAO richiesteManutenzioneDAO = new RichiesteManutenzioneDAO(connection);
            List<RichiestaManutenzione> richieste =richiesteManutenzioneDAO.getRichiesteManutenzioneDaAccettare();
            for(RichiestaManutenzione ric :richieste){
                RichiestaManutenzioneDTO dto = new RichiestaManutenzioneDTO();
                dto.setIdManutenzione(ric.getIdManutenzione());
                dto.setAccettata(ric.getRichiestaAccettata());
                dto.setIdAdminAzienda(ric.getIdAdmin());
                dto.setCompletata(ric.getRichiestaCompletata());
                dto.setIdVeicolo(ric.getIdVeicolo());
                dto.setDataRichiesta(ric.getDataRichiesta());
                dto.setTipoManutenzione(ric.getTipoManutenzione());
                richiesteDTO.add(dto);
            }
        }
        return richiesteDTO;
    }

    public RichiestaManutenzioneDTO getRichiesteManutenzioneById(int id) throws SQLException {
        RichiestaManutenzioneDTO dto = new RichiestaManutenzioneDTO();
        try(Connection connection=this.dataSource.getConnection()){
            RichiesteManutenzioneDAO dao = new RichiesteManutenzioneDAO(connection);
            RichiestaManutenzione richiesta = dao.getRichiestaManutenzione(id);
            if(richiesta!=null){
                dto.setIdManutenzione(id);
                dto.setIdAdminAzienda(richiesta.getIdAdmin());
                dto.setIdVeicolo(richiesta.getIdVeicolo());
                dto.setDataRichiesta(richiesta.getDataRichiesta());
                dto.setTipoManutenzione(richiesta.getTipoManutenzione());
                dto.setAccettata(richiesta.getRichiestaAccettata());
                dto.setCompletata(richiesta.getRichiestaCompletata());
                Veicolo veicolo = richiesta.getVeicolo();
                VeicoloDTO veicoloDTO = new VeicoloDTO();
                veicoloDTO.setTargaVeicolo(veicolo.getTargaVeicolo());
                veicoloDTO.setModello(veicolo.getModello());
                veicoloDTO.setNomeAziendaAffiliata(veicolo.getNomeAziendaAffiliata());
                dto.setVeicolo(veicoloDTO);
                return dto;
            }
            return null;
        }
    }

}
