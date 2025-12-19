package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.StatisticheDipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.AziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.LuogoAziendaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaNoleggioDAO;
import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DipendenteService {
    @Autowired private DataSource dataSource;

    public String getNomeDipendente(Integer idDipendente) throws SQLException{
        try(Connection connection = dataSource.getConnection()){
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            Dipendente utente = utenteDAO.getDipendenteDaId(idDipendente);
            return utente.getNomeUtente();
        }
    }

    public RichiestaNoleggioDTO getProssimoNoleggioDipendente(Integer idDipendente) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            RichiestaNoleggio richiesta = dao.getProssimaRichiestaNoleggioDipendente(idDipendente);
            if(richiesta != null) {
                RichiestaNoleggioDTO dto = new RichiestaNoleggioDTO();
                dto.setDataRitiro(richiesta.getDataRitiro());
                dto.setOraInizio(richiesta.getOraInizio());
                dto.setOraFine(richiesta.getOraFine());
                dto.setMotivazione(richiesta.getMotivazione());
                dto.setStatoRichiesta(richiesta.getStatoRichiesta());
                Veicolo veicolo = richiesta.getVeicolo();
                VeicoloDTO veicoloDTO = new VeicoloDTO(veicolo,false);
                dto.setVeicolo(veicoloDTO);

                return dto;
            }
        }
        return null;
    }

    public StatisticheDipendenteDTO getStatisticheDipendente(Integer idDipendente)throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            return dao.getStatisticheDipendente(idDipendente);
        }
    }

    public List<RichiestaNoleggioDTO> getRichiesteNoleggioDipendente(Integer idDipendente) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            List<RichiestaNoleggio> richieste =dao.getRichiesteNoleggioDipendente(idDipendente);
            List<RichiestaNoleggioDTO> richiesteDTO = new ArrayList<>();
            for (RichiestaNoleggio ric :richieste) {
                RichiestaNoleggioDTO dto = new RichiestaNoleggioDTO(ric,false);
                Veicolo veicolo = ric.getVeicolo();
                VeicoloDTO veicoloDTO = new VeicoloDTO(veicolo,false);
                dto.setVeicolo(veicoloDTO);
                richiesteDTO.add(dto);
            }
            return richiesteDTO;
        }
    }
    public List<LuogoDTO> getLuoghiAziendaAssociata(Integer idAziendaAssociata) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            LuogoAziendaDAO dao = new LuogoAziendaDAO(connection);
            List<LuogoAzienda> luoghi=dao.getLuogiDisponibiliPerAzienda(idAziendaAssociata);
            List<LuogoDTO> luoghiDTO = new ArrayList<>();
            for (LuogoAzienda luogo : luoghi) {
                LuogoDTO dto= new LuogoDTO(luogo);
                luoghiDTO.add(dto);
            }
            return luoghiDTO;
        }
    }
}
