package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.StatisticheDipendenteDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiestaNoleggioDAO;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DipendenteService {
    @Autowired private DataSource dataSource;

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
        System.out.println("Sono nel service");
        try (Connection connection = dataSource.getConnection()) {
            RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
            return dao.getStatisticheDipendente(idDipendente);
        }
    }
}
