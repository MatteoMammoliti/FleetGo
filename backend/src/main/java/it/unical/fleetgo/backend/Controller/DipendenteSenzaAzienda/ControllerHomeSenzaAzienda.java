package it.unical.fleetgo.backend.Controller.DipendenteSenzaAzienda;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiAzienda;
import it.unical.fleetgo.backend.Service.DipendenteNoAziendaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/homeNoAzienda")
public class ControllerHomeSenzaAzienda {

    @Autowired DipendenteNoAziendaService service;

    @GetMapping("/getAziende")
    public ResponseEntity<List<ContenitoreDatiAzienda>> getAziende() throws SQLException {
        return ResponseEntity.ok(this.service.getInfoAziende());
    }

    @GetMapping("/getRichiestaAttesa")
    public ResponseEntity<ContenitoreDatiAzienda> getRichiestaAttesa(HttpSession session) throws SQLException {

        Integer idUtente =  (Integer) session.getAttribute("idUtente");

        if(idUtente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(this.service.getRichiestaInAttesa(idUtente));
    }

    @PostMapping("/annullaRichiestaAttesa{azienda}")
    public ResponseEntity<String> rimuoviRichiestaAttesa(@PathVariable Integer azienda, HttpSession session) throws SQLException {
        Integer idUtente = (Integer)session.getAttribute("idUtente");

        if(idUtente == null || azienda==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        this.service.eliminaRichiestaInAttesa(idUtente,azienda);
        return ResponseEntity.ok("Rimozione avvenuta con successo");
    }

    @PostMapping("/inviaRichiestaAffiliazione{azienda}")
    public ResponseEntity<String> inviaRichiestaAffiliazione(@PathVariable Integer azienda, HttpSession session) throws SQLException {
        Integer idUtente=(Integer)session.getAttribute("idUtente");

        if(idUtente==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorizzato ad effettuare la richiesta");
        }

        this.service.aggiungiRichiestaAffiliazione(idUtente,azienda);
        return ResponseEntity.ok("Richiesta avvenuta con successo");
    }
}