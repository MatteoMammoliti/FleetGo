package it.unical.fleetgo.backend.Controller.Dipendente;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Service.DipendenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerPrenotazioniDipendente {

    @Autowired private DipendenteService dipendenteService;

    @GetMapping("/leMiePrenotazioni")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getLeMiePrenotazioni(HttpSession session){
        try{
            return ResponseEntity.ok(this.dipendenteService.getRichiesteNoleggioDipendente((Integer) session.getAttribute("idUtente")));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
}
