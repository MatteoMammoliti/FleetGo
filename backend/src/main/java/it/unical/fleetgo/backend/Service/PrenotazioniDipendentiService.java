package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloPrenotazioneDTO;
import it.unical.fleetgo.backend.Persistence.DAO.GestioneVeicoloAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.LuogoAziendaDAO;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.VeicoloPrenotazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrenotazioniDipendentiService {
    @Autowired private DataSource dataSource;

    public List<VeicoloPrenotazioneDTO> getVeicoli(Integer idAzienda,String dataInizio,String dataFine,String oraInizio,String oraFine,String nomeLuogo) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            GestioneVeicoloAziendaDAO dao = new GestioneVeicoloAziendaDAO(connection);
            List<VeicoloPrenotazione> veicoli = dao.veicoliPrenotazione(idAzienda,dataInizio,dataFine,oraInizio,oraFine,nomeLuogo);
            List<VeicoloPrenotazioneDTO> dto = new ArrayList<>();
            for(VeicoloPrenotazione v : veicoli){
                VeicoloPrenotazioneDTO veicoloDTO = new VeicoloPrenotazioneDTO();
                veicoloDTO.setStatoAttuale(v.getStatoAttuale());
                veicoloDTO.setModello(v.getModello());
                veicoloDTO.setTargaVeicolo(v.getTargaVeicolo());
                veicoloDTO.setTipoDistribuzioneVeicolo(v.getTipoDistribuzioneVeicolo());
                veicoloDTO.setLivelloCarburante(v.getLivelloCarburante());
                LuogoDTO luogoDTO = new LuogoDTO();
                luogoDTO.setNomeLuogo(v.getLuogo().getNomeLuogo());
                veicoloDTO.setLuogoRitiroDeposito(luogoDTO);
                dto.add(veicoloDTO);
            }
            return dto;
        }
    }

    public List<LuogoDTO> getLuogoAzienda(Integer idAzienda) throws SQLException {
        try(Connection connection=dataSource.getConnection()){
            LuogoAziendaDAO dao = new LuogoAziendaDAO(connection);
            List<LuogoAzienda> luoghi = dao.getLuogiDisponibiliPerAzienda(idAzienda);
            List<LuogoDTO> dto=new ArrayList<>();
            for(LuogoAzienda l : luoghi){
                LuogoDTO luogoDTO = new LuogoDTO(l);
                dto.add(luogoDTO);

            }
            return dto;
        }
    }
}
