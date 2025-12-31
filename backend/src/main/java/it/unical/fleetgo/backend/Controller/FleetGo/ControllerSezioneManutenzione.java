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
public class ControllerSezioneManutenzione {
    @Autowired private FleetGoService fleetGoService;

    @GetMapping("/datiManutenzioni")
    public ResponseEntity<ContenitoreStatisticheNumericheManutezioni> datiManutenzioni() throws SQLException {
        return ResponseEntity.ok(this.fleetGoService.getStatisticheManutenzioni());
    }

    @GetMapping("/manutenzioniInCorso")
    public ResponseEntity<List<RichiestaManutenzioneDTO>> getManutenzioniInCorso() throws SQLException {
        return ResponseEntity.ok(this.fleetGoService.getRichiesteManutenzioniInCorso());
    }

    @GetMapping("/manutenzioniStorico")
    public ResponseEntity<List<RichiestaManutenzioneDTO>> getStoricoManutenzioni() throws SQLException {
        return ResponseEntity.ok(this.fleetGoService.getRichiesteManutenzioniStorico());
    }

    @PostMapping("/chiudiRichiestaManutenzione{idRichiesta}")
    public ResponseEntity<String> chiudiRichiestaManutenzione(@PathVariable Integer idRichiesta) throws SQLException {
        this.fleetGoService.concludiRichiestaManutenzione(idRichiesta);
        return ResponseEntity.ok("Richiesta conclusa con successo");
    }
}