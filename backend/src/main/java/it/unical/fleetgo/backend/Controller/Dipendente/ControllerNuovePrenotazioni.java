package it.unical.fleetgo.backend.Controller.Dipendente;

import it.unical.fleetgo.backend.Exceptions.DatePrenotazioneNonValide;
import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloPrenotazioneDTO;
import it.unical.fleetgo.backend.Service.AziendaService;
import it.unical.fleetgo.backend.Service.PrenotazioniDipendentiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboardDipendente")
public class ControllerNuovePrenotazioni {

    @Autowired private AziendaService aziendaService;
    @Autowired private PrenotazioniDipendentiService service;

    @GetMapping("/caricaVeicoli")
    public ResponseEntity<List<VeicoloPrenotazioneDTO>> getVeicoli(@RequestParam("ritiro") String dataRitiro,
                                                                   @RequestParam("consegna") String dataConsegna,
                                                                   @RequestParam("oraInizio")String oraInizio,
                                                                   @RequestParam("oraFine") String oraFine,
                                                                   @RequestParam("nomeLuogo") String nomeLuogo,
                                           HttpSession session) throws SQLException {
        Integer idAziendaAssociata= (Integer) session.getAttribute("idAziendaAssociata");

        if(idAziendaAssociata==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (LocalDate.parse(dataRitiro).isAfter(LocalDate.parse(dataConsegna))) {
            throw new DatePrenotazioneNonValide();
        }

        return ResponseEntity.ok(service.getVeicoli(
                idAziendaAssociata,dataRitiro,dataConsegna,oraInizio,oraFine,nomeLuogo)
        );
    }

    @GetMapping("/caricaLuoghi")
    public ResponseEntity<List<LuogoDTO>> getLuoghiAzienda(HttpSession session) throws SQLException {
        Integer idAziendaAssociata= (Integer) session.getAttribute("idAziendaAssociata");
        if(idAziendaAssociata==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(service.getLuogoAzienda(idAziendaAssociata));
    }

    @PostMapping("/inviaRichiesta")
    public ResponseEntity<String> inviaNuovaRichiesta(@RequestBody RichiestaNoleggioDTO richiesta, HttpSession session) throws SQLException {
        Integer idDipendente= (Integer) session.getAttribute("idUtente");
        Integer idAzienda=(Integer) session.getAttribute("idAziendaAssociata");

        if(idDipendente==null || idAzienda==null || !aziendaService.isAziendaAttiva(idAzienda)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (richiesta.getIdVeicolo() == null) {
            throw new IllegalArgumentException("Veicolo obbligatorio");
        }

        richiesta.setIdDipendente(idDipendente);
        richiesta.setIdAziendaRiferimento(idAzienda);
        this.service.inviaRichiestaNoleggio(richiesta);
        return ResponseEntity.status(HttpStatus.CREATED).body("Richiesta generata con successo");
    }
}