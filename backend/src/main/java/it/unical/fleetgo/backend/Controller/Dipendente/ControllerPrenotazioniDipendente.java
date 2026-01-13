package it.unical.fleetgo.backend.Controller.Dipendente;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Service.DipendenteService;
import it.unical.fleetgo.backend.Service.PrenotazioniDipendentiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardDipendente")
public class ControllerPrenotazioniDipendente {

    @Autowired private DipendenteService dipendenteService;
    @Autowired private PrenotazioniDipendentiService prenotazioniDipendentiService;

    @GetMapping("/leMiePrenotazioni")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getLeMiePrenotazioni(HttpSession session) throws SQLException {

        Integer idAzienda=(Integer)session.getAttribute("idAziendaAssociata");
        Integer idUtente =  (Integer) session.getAttribute("idUtente");

        if(idAzienda==null || idUtente==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(this.dipendenteService.getRichiesteNoleggioDipendente(
                idUtente,
                idAzienda)
        );
    }

    @PostMapping("/eliminaRichiesta")
    public ResponseEntity<String> eliminaRichiesta(@RequestBody Integer idRichiesta, HttpSession session) throws SQLException {
        Integer idDipendente=(Integer) session.getAttribute("idUtente");

        if(idDipendente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        this.prenotazioniDipendentiService.eliminaRichiesta(idRichiesta, idDipendente);
        return ResponseEntity.ok("Prenotazione eliminata con successo");
    }
}