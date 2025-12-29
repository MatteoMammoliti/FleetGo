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
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerHomeSenzaAzienda {

    @Autowired DipendenteNoAziendaService service;

    @GetMapping("/getAziende")
    public ResponseEntity<List<ContenitoreDatiAzienda>> getAziende(){
        try{
            return ResponseEntity.ok(this.service.getInfoAziende());
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/getRichiestaAttesa")
    public ResponseEntity<ContenitoreDatiAzienda> getRichiestaAttesa(HttpSession session){
        try{
            return ResponseEntity.ok(this.service.getRichiestaInAttesa((Integer)session.getAttribute("idUtente")));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/annullaRichiestaAttesa{azienda}")
    public ResponseEntity<String> rimuoviRichiestaAttesa(@PathVariable Integer azienda, HttpSession session){
        Integer idUtente = (Integer)session.getAttribute("idUtente");
        if(idUtente == null || azienda==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            boolean riuscito =this.service.eliminaRichiestaInAttesa(idUtente,azienda);
            if(riuscito){
                return ResponseEntity.ok("Rimozione avvenuta con successo");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la rimozione");
            }
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno durante la rimozione");
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Richiesta errata");
        }
    }

    @PostMapping("/inviaRichiestaAffiliazione{azienda}")
    public ResponseEntity<String> inviaRichiestaAffiliazione(@PathVariable Integer azienda, HttpSession session){
        Integer idUtente=(Integer)session.getAttribute("idUtente");
        if(idUtente==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autorizzato ad effettuare la richiesta");
        }
        try{
            Boolean successo= this.service.aggiungiRichiestaAffiliazione(idUtente,azienda);
            if(successo){
                return ResponseEntity.ok("Richiesta avvenuta con successo");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante l'aggiunta");
            }
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno interno durante l'aggiunta");
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Richiesta errata");
        }
    }
}
