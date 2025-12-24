package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumericheManutezioni;
import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Service.FleetGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerSezioneManutenzione {
    @Autowired private FleetGoService fleetGoService;

    @GetMapping("/datiManutenzioni")
    public ResponseEntity<ContenitoreStatisticheNumericheManutezioni> datiManutenzioni() {
        try{
            return ResponseEntity.ok(this.fleetGoService.getStatisticheManutenzioni());
        }catch(SQLException e){
            return ResponseEntity.internalServerError().build();
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/manutenzioniInCorso")
    public ResponseEntity<List<RichiestaManutenzioneDTO>> getManutenzioniInCorso() {
        try{
            return ResponseEntity.ok(this.fleetGoService.getRichiesteManutenzioniInCorso());
        }catch (SQLException e){
            return ResponseEntity.internalServerError().build();
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/manutenzioniStorico")
    public ResponseEntity<List<RichiestaManutenzioneDTO>> getStoricoManutenzioni() {
        try{
            return ResponseEntity.ok(this.fleetGoService.getRichiesteManutenzioniStorico());
        }catch (SQLException e){
            return ResponseEntity.internalServerError().build();
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/chiudiRichiestaManutenzione{idRichiesta}")
    public ResponseEntity<String> chiudiRichiestaManutenzione(@PathVariable Integer idRichiesta) {
        try{
            Boolean operazioneAvvenutaConSuccesso= this.fleetGoService.concludiRichiestaManutenzione(idRichiesta);
            if(operazioneAvvenutaConSuccesso){
                return ResponseEntity.ok("Richiesta conclusa con successo");
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell richiesta");
            }
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Errore interno nell richiesta");
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Richiesta non valida");
        }

    }
}
