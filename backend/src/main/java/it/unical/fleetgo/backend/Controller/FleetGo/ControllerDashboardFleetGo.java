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
public class ControllerDashboardFleetGo {

    @Autowired private UtenteService utenteService;
    @Autowired private FleetGoService fleetGoService;


    @GetMapping("/statistiche")
    public ResponseEntity<ContenitoreStatisticheNumeriche> getStatisticheNumeriche() throws SQLException {
        return ResponseEntity.ok(utenteService.getStatisticheNumeriche());
    }

    @GetMapping("/fattureDaGenerare")
    public ResponseEntity<List<FatturaDaGenerareDTO>> getFattureDagenerare() throws SQLException {
        return ResponseEntity.ok(fleetGoService.getGeneraFattura());
    }

    @GetMapping("/richiesteManutezioneDaAccettare")
    public ResponseEntity<List<RichiestaManutenzioneDTO>> getRichiesteManutenzioneDaAccettare() throws SQLException {
        return ResponseEntity.ok(fleetGoService.getRichiesteManutenzioneDaAccettare());
    }

    @GetMapping("/dettagliRichiestaManutenzone/{id}")
    public ResponseEntity<RichiestaManutenzioneDTO> getDettagliRichiestaManutenzione(@PathVariable Integer id) throws SQLException {

        RichiestaManutenzioneDTO richiesta = fleetGoService.getRichiesteManutenzioneById(id);

        if (richiesta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(richiesta);
    }

    @PostMapping("/accettaRichiestaManutenzione/{id}")
    public ResponseEntity<String> accettaRichiestaManutenzione(@PathVariable Integer id) throws SQLException {
        fleetGoService.accettaRichiestaManutenzione(id);
        return ResponseEntity.ok("Richiesta accettata con successo");
    }

    @PostMapping("/rifiutaRichiestaManutenzione/{id}")
    public ResponseEntity<String> rifiutaRichiestaManutenzione(@PathVariable Integer id) throws SQLException {
        fleetGoService.rifiutaRichiestaManutenzione(id);
        return ResponseEntity.ok("Richiesta rifiutata con successo");
    }

    @PostMapping("/generaFattura")
    public ResponseEntity<String> generaFattura(@RequestBody FatturaDaGenerareDTO fattura) throws SQLException {
        fleetGoService.generaFattura(fattura);
        return ResponseEntity.ok("Fattura generata con successo");
    }
}