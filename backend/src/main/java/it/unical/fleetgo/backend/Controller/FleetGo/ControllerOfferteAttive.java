package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.OffertaDTO;
import it.unical.fleetgo.backend.Service.FleetGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
public class ControllerOfferteAttive {

    @Autowired FleetGoService fleetGoService;

    @PostMapping("/inserisciOfferta")
    public ResponseEntity<String> inserisciNuovaOfferta(@RequestBody OffertaDTO offerta) {
        try {
            fleetGoService.inserisciNuovaOfferta(offerta);
            return ResponseEntity.status(HttpStatus.OK).body("Inserimento offerta riuscito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inserimento non riuscito");
        }
    }

    @PostMapping("/eliminaOfferta")
    public ResponseEntity<String> eliminaOfferta(@RequestBody Integer idOfferta) {
        try {
            fleetGoService.eliminaOfferta(idOfferta);
            return ResponseEntity.status(HttpStatus.OK).body("Eliminazione offerta riuscito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inserimento non riuscito");
        }
    }

    @GetMapping("/getOfferte")
    public ResponseEntity<List<OffertaDTO>> getOfferteAttive() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    fleetGoService.getOfferteAttive()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}