package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Exceptions.FattureDaGenerare;
import it.unical.fleetgo.backend.Exceptions.NoleggiInCorsoPresenti;
import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Persistence.DAO.*;
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

    public void riabilitaAzienda(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO =  new AziendaDAO(connection);
            aziendaDAO.gestisciAttivitaAzienda(idAzienda, true);
        }
    }

    public void disabilitaAzienda(Integer idAzienda) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            GestioneVeicoloAziendaDAO gestioneDAO = new GestioneVeicoloAziendaDAO(connection);
            RichiestaNoleggioDAO richiestaNoleggioDAO = new RichiestaNoleggioDAO(connection);
            RichiestaAffiliazioneAziendaDAO richiestaAffiliazioneAziendaDAO = new RichiestaAffiliazioneAziendaDAO(connection);
            GeneraFatturaDAO generaFatturaDAO  = new GeneraFatturaDAO(connection);

            connection.setAutoCommit(false);
            try{
                richiestaNoleggioDAO.aggiornaStatiNoleggi();

                if(generaFatturaDAO.ciSonoFattureDaGenerare(idAzienda)){
                    connection.rollback();
                    throw new FattureDaGenerare();
                }

                if(richiestaNoleggioDAO.getRichiesteAccettateEInCorso(idAzienda)) {
                    connection.rollback();
                    throw new NoleggiInCorsoPresenti();
                }

                richiestaNoleggioDAO.eliminaRichiesteInAttesa(idAzienda);
                gestioneDAO.contrassegnaVeicoliLiberiPreDisabilitazioneAzienda(idAzienda);
                gestioneDAO.eliminaVeicoliInGestioneAzienda(idAzienda);
                richiestaAffiliazioneAziendaDAO.rimuoviDipendentiDaAziendaDisabilitata(idAzienda);
                aziendaDAO.gestisciAttivitaAzienda(idAzienda, false);


                connection.commit();
            }catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<AziendaDTO> getElencoAziendeAttive() throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            List<Azienda> elencoAziende = aziendaDAO.getAziendeAttiveInPiattaforme();

            List<AziendaDTO> listaAziende = new ArrayList<>();
            for(Azienda a : elencoAziende) {
                AziendaDTO aziendaDTO = new AziendaDTO(a);
                listaAziende.add(aziendaDTO);
            }
            return listaAziende;
        }
    }

    public List<AziendaDTO> getElencoAziendeDisabilitate() throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            List<Azienda> elencoAziende = aziendaDAO.getAziendeDisabilitateInPiattaforme();

            List<AziendaDTO> listaAziende = new ArrayList<>();
            for(Azienda a : elencoAziende) {
                AziendaDTO aziendaDTO = new AziendaDTO(a);
                listaAziende.add(aziendaDTO);
            }
            return listaAziende;
        }
    }

    public boolean impostaSede(Integer idLuogo, Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            return aziendaDAO.impostaSedeAzienda(idLuogo, idAzienda);
        }
    }

    public boolean isAziendaAttiva(Integer idAzienda) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            AziendaDAO aziendaDAO = new AziendaDAO(connection);
            return aziendaDAO.isAziendaAttiva(idAzienda);
        }
    }
}