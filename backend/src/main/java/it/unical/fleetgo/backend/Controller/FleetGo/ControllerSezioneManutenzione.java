package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumericheManutezioni;
import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Service.FleetGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
