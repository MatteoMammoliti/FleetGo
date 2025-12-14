package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminAziendaleService {

    @Autowired private DataSource dataSource;

    public void modificaDati(ModificaDatiUtenteDTO dati) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            utenteDAO.modificaDatiUtente(dati);
        }
    }

    public List<DipendenteDTO> getDipendenti(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO =
                    new RichiestaAffiliazioneAziendaDAO(connection);

            List<Dipendente> dipendenti = richiestaAffiliazioneAziendaDAO.getDipendentiAzienda(idAzienda);
            List<DipendenteDTO> listaDipendenti = new ArrayList<>();

            for(Dipendente d : dipendenti) {
                DipendenteDTO dipendenteDTO = new DipendenteDTO(d);
                listaDipendenti.add(dipendenteDTO);
            }

            return listaDipendenti;
        }
    }

    public Integer getIdAziendaGestita(Integer idAdmin) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            return aziendaDAO.getIdAziendaGestita(idAdmin);
        }
    }

    public void rimuoviDipendente(Integer idUtente, Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO =
                    new RichiestaAffiliazioneAziendaDAO(connection);
            richiestaAffiliazioneAziendaDAO.rimuoviRichiestaAffiliazioneAzienda(idUtente, idAzienda);
        }
    }

    public List<LuogoDTO> getLuoghiCorrenti(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            LuogoAziendaDAO luogoAziendaDAO = new LuogoAziendaDAO(connection);

            List<LuogoAzienda> l = luogoAziendaDAO.getLuogiDisponibiliPerAzienda(idAzienda);
            List<LuogoDTO> luoghi = new ArrayList<>();

            for(LuogoAzienda luogo : l) {
                luoghi.add(new LuogoDTO(luogo));
            }
            return luoghi;
        }
    }

    public void aggiungiLuogo(LuogoDTO luogo) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            LuogoAziendaDAO luogoAziendaDAO = new LuogoAziendaDAO(connection);
            luogoAziendaDAO.inserisciLuogo(luogo);
        }
    }
}