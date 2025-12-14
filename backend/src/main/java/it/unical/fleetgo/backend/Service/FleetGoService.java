package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.*;
import it.unical.fleetgo.backend.Persistence.DAO.FatturaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.GeneraFatturaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.OffertaDAO;
import it.unical.fleetgo.backend.Persistence.DAO.RichiesteManutenzioneDAO;
import it.unical.fleetgo.backend.Persistence.Entity.Fattura;
import it.unical.fleetgo.backend.Persistence.Entity.Offerta;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaManutenzione;
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
    @Autowired private GeneratorePdfService generatorePdfService;

    public List<FatturaDaGenerareDTO> getGeneraFattura() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()){
            GeneraFatturaDAO generaFatturaDAO = new GeneraFatturaDAO(connection);
            return generaFatturaDAO.generaFatturaDaGenerare();
        }
    }
    public List<RichiestaManutenzioneDTO> getRichiesteManutenzioneDaAccettare() throws SQLException {
        List<RichiestaManutenzioneDTO> richiesteDTO = new ArrayList<>();
        try(Connection connection=this.dataSource.getConnection()){
            RichiesteManutenzioneDAO richiesteManutenzioneDAO = new RichiesteManutenzioneDAO(connection);
            List<RichiestaManutenzione> richieste =richiesteManutenzioneDAO.getRichiesteManutenzioneDaAccettare();
            for(RichiestaManutenzione ric :richieste){
                RichiestaManutenzioneDTO dto = new RichiestaManutenzioneDTO(ric,true);
                richiesteDTO.add(dto);
            }
        }
        return richiesteDTO;
    }

    public RichiestaManutenzioneDTO getRichiesteManutenzioneById(int id) throws SQLException {

        try(Connection connection=this.dataSource.getConnection()){
            RichiesteManutenzioneDAO dao = new RichiesteManutenzioneDAO(connection);
            RichiestaManutenzione richiesta = dao.getRichiestaManutenzione(id);
            if(richiesta!=null){
                RichiestaManutenzioneDTO dto = new RichiestaManutenzioneDTO(richiesta,true);
                dto.setIdManutenzione(id);
                return dto;
            }
            return null;
        }
    }

    public boolean accettaRichiestaManutenzione(Integer idManutenzione)throws SQLException {
        try(Connection connection=this.dataSource.getConnection()){
            RichiesteManutenzioneDAO dao = new RichiesteManutenzioneDAO(connection);
            return dao.contrassegnaRichiestaManutenzione(idManutenzione,true);
        }

    }

    public boolean rifiutaRichiestaManutenzione(Integer idManutenzione) throws SQLException {
        try(Connection connection=this.dataSource.getConnection()){
            RichiesteManutenzioneDAO dao = new RichiesteManutenzioneDAO(connection);
            return dao.contrassegnaRichiestaManutenzione(idManutenzione,false);
        }
    }

    public List<Integer> getAnni() throws SQLException {
        try(Connection connection=this.dataSource.getConnection()){
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            return fatturaDAO.getAnniFatture();
        }
    }

    public List<FatturaDTO> getFatturePerAnno(Integer idAnno) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()){
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            List<Fattura> f = fatturaDAO.getFattureEmesseDaFleetGo(idAnno);
            List<FatturaDTO> fatture = new ArrayList<>();

            for(Fattura fattura : f) {
                fatture.add(new FatturaDTO(fattura,true));
            }

            return fatture;
        }
    }

    public byte[] downloadFattura(Integer idAnno) throws SQLException{

        try(Connection connection = this.dataSource.getConnection()) {
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            Fattura f = fatturaDAO.getFatturaByNumeroFattura(idAnno);

            if(f != null) {

                return this.generatorePdfService.generaPdfFattura(
                        new FatturaDTO(f, true)
                );
            }
        }
        return null;
    }

    public void generaFattura(FatturaDaGenerareDTO fattura) throws SQLException {

        try(Connection connection = this.dataSource.getConnection()) {
            FatturaDAO fatturaDAO = new FatturaDAO(connection);
            fatturaDAO.inserisciFattura(new FatturaDTO(fattura));
        }
    }
    public ContenitoreStatisticheNumericheManutezioni getStatisticheManutenzioni() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiesteManutenzioneDAO dao = new RichiesteManutenzioneDAO(connection);
            return dao.getStatisticheManutenzioni();
        }
    }
    public List<RichiestaManutenzioneDTO> getRichiesteManutenzioniInCorso() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiesteManutenzioneDAO dao = new RichiesteManutenzioneDAO(connection);
            List<RichiestaManutenzione> richieste =dao.getRichiesteManutenzioneInCorso();
            List<RichiestaManutenzioneDTO> richiesteDTO = new ArrayList<>();
            for(RichiestaManutenzione ric: richieste){
                RichiestaManutenzioneDTO dto = new RichiestaManutenzioneDTO(ric,true);
                richiesteDTO.add(dto);
            }
            return richiesteDTO;
        }
    }
    public List<RichiestaManutenzioneDTO> getRichiesteManutenzioniStorico() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            RichiesteManutenzioneDAO dao = new RichiesteManutenzioneDAO(connection);
            List<RichiestaManutenzione> richieste = dao.getRichiesteManutenzioneStorico();
            List<RichiestaManutenzioneDTO> richiesteDTO = new ArrayList<>();
            for(RichiestaManutenzione ric: richieste){
                RichiestaManutenzioneDTO dto = new RichiestaManutenzioneDTO(ric,true);
                richiesteDTO.add(dto);
            }
        return richiesteDTO;
        }
    }

    public void inserisciNuovaOfferta(OffertaDTO offertaDTO) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            OffertaDAO offertaDAO = new OffertaDAO(connection);
            offertaDAO.inserisciOfferta(offertaDTO);
        }
    }

    public void eliminaOfferta(Integer idOfferta) throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            OffertaDAO offertaDAO = new OffertaDAO(connection);
            offertaDAO.eliminaOfferta(idOfferta);
        }
    }

    public List<OffertaDTO> getOfferteAttive() throws SQLException {
        try(Connection connection = this.dataSource.getConnection()) {
            OffertaDAO offertaDAO = new OffertaDAO(connection);
            List<Offerta> offerte = offertaDAO.getOfferteAttive();
            List<OffertaDTO> offerteDTO = new ArrayList<>();

            for(Offerta off : offerte) {
                offerteDTO.add(new OffertaDTO(off));
            }

            return offerteDTO;
        }
    }
}