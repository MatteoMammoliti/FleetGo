package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloPrenotazioneDTO;
import it.unical.fleetgo.backend.Persistence.DAO.GestioneVeicoloAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.LuogoAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaNoleggioDAO;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.VeicoloPrenotazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrenotazioniDipendentiService {

    @Autowired private DataSource dataSource;

    private final Double TARRIFA = 0.15;

    public List<VeicoloPrenotazioneDTO> getVeicoli(Integer idAzienda,String dataInizio,String dataFine,String oraInizio,String oraFine,String nomeLuogo) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            GestioneVeicoloAziendaDAO dao = new GestioneVeicoloAziendaDAO(connection);
            List<VeicoloPrenotazione> veicoli = dao.veicoliPrenotazione(idAzienda,dataInizio,dataFine,oraInizio,oraFine,nomeLuogo);
            List<VeicoloPrenotazioneDTO> dto = new ArrayList<>();
            for(VeicoloPrenotazione v : veicoli){
                VeicoloPrenotazioneDTO veicoloDTO = new VeicoloPrenotazioneDTO();
                veicoloDTO.setIdVeicolo(v.getIdVeicolo());
                veicoloDTO.setStatoAttuale(v.getStatoAttuale());
                veicoloDTO.setIdModello(v.getIdModello());
                veicoloDTO.setNomeModello(v.getNomeModello());
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

    public Integer inviaRichiestaNoleggio(RichiestaNoleggioDTO richiestaNoleggio) throws SQLException {
        try(Connection connection=dataSource.getConnection()){
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            Double prezzoStimato=this.calcoloPrezzoRichiesta(richiestaNoleggio);
            return dao.aggiungiRichiestaNoleggio(richiestaNoleggio,prezzoStimato);
        }
    }

    public boolean eliminaRichiesta(Integer idRichiestaNoleggio) throws SQLException {
        try(Connection connection=dataSource.getConnection()){
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            return dao.rimuoviRichiestaNoleggio(idRichiestaNoleggio);
        }
    }


    private Double calcoloPrezzoRichiesta(RichiestaNoleggioDTO richiesta){
        LocalDateTime inizio = LocalDateTime.parse(richiesta.getDataRitiro() + "T" + richiesta.getOraInizio());
        LocalDateTime fine = LocalDateTime.parse(richiesta.getDataConsegna()+ "T" + richiesta.getOraFine());
        long minutiTotali = ChronoUnit.MINUTES.between(inizio, fine);
        if (minutiTotali <= 0) {
            throw new RuntimeException("Le date inserite non sono valide (fine prima dell'inizio)");
        }
        return minutiTotali * this.TARRIFA;

    }

}
