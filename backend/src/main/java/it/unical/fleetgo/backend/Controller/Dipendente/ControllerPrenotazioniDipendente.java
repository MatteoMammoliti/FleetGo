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
@RequestMapping("/prenotazioni")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerPrenotazioniDipendente {

    @Autowired private DipendenteService dipendenteService;
    @Autowired private PrenotazioniDipendentiService prenotazioniDipendentiService;

    @GetMapping("/leMiePrenotazioni")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getLeMiePrenotazioni(HttpSession session){
        Integer idAzienda=(Integer)session.getAttribute("idAziendaAssociata");
        if(idAzienda==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(this.dipendenteService.getRichiesteNoleggioDipendente((Integer) session.getAttribute("idUtente"),idAzienda));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/eliminaRichiesta")
    public ResponseEntity eliminaRichiesta(@RequestBody Integer idRichiesta, HttpSession session) {
        Integer idDipendente=(Integer) session.getAttribute("idUtente");
        System.out.println(idDipendente);
        if(idDipendente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try{
            return ResponseEntity.ok(this.prenotazioniDipendentiService.eliminaRichiesta(idRichiesta));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
}
