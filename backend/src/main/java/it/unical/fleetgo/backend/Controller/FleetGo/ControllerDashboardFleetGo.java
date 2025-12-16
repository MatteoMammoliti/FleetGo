package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.FatturaDaGenerareDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Service.FleetGoService;
import it.unical.fleetgo.backend.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerDashboardFleetGo {

    @Autowired private UtenteService utenteService;
    @Autowired private FleetGoService fleetGoService;


    @GetMapping("/statistiche")
    public ResponseEntity<ContenitoreStatisticheNumeriche> getStatisticheNumeriche(){
        try{
            return ResponseEntity.ok(utenteService.getStatisticheNumeriche());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/fattureDaGenerare")
    public ResponseEntity<List<FatturaDaGenerareDTO>> getFattureDagenerare(){
        try{
            return ResponseEntity.ok(fleetGoService.getGeneraFattura());
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (SQLException sqlException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/richiesteManutezioneDaAccettare")
    public ResponseEntity<List<RichiestaManutenzioneDTO>> getRichiesteManutenzioneDaAccettare(){
        try{
            return ResponseEntity.ok(fleetGoService.getRichiesteManutenzioneDaAccettare());
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (SQLException sqlException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/dettagliRichiestaManutenzone/{id}")
    public ResponseEntity<RichiestaManutenzioneDTO> getDettagliRichiestaManutenzione(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(fleetGoService.getRichiesteManutenzioneById(id));
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (SQLException sqlException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PostMapping("/accettaRichiestaManutenzione/{id}")
    public ResponseEntity<String> accettaRichiestaManutenzione(@PathVariable Integer id){
        try{
            boolean esitoOperazione = fleetGoService.accettaRichiestaManutenzione(id);
            if(esitoOperazione){
                return ResponseEntity.ok("Richiesta accettata con successo");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Richiesta non valida");
            }
        }catch (SQLException sqlException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/rifiutaRichiestaManutenzione/{id}")
    public ResponseEntity<String> rifiutaRichiestaManutenzione(@PathVariable Integer id){
        try{
            boolean esitoOperazione = fleetGoService.rifiutaRichiestaManutenzione(id);
            if(esitoOperazione){
                return ResponseEntity.ok("Richiesta rifiutata con successo");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Richiesta non valida");
            }
        }catch (SQLException sqlException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/generaFattura")
    public ResponseEntity<String> generaFattura(@RequestBody FatturaDaGenerareDTO fattura){
        try {
            System.out.printf("ho ricevuto" + fattura.getIdOffertaApplicata());
            fleetGoService.generaFattura(fattura);
            return ResponseEntity.ok("Fattura generata con successo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}