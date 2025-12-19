package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.VeicoloService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerPrenotazioni {

    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping(value = "/getPrenotazioni")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getPrenotazioni(HttpSession session) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getPrenotazioni(
                            (Integer) session.getAttribute("idAzienda")
                    )
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getPrenotazioni/{idRichiesta}")
    public ResponseEntity<RichiestaNoleggioDTO> getPrenotazioni(@PathVariable Integer idRichiesta) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getRichiestaNoleggio(idRichiesta)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getPrenotazioniDaAccettare")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getPrenotazioniDaAccettare(HttpSession session) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getRichiesteDaAccettare(
                            (Integer) session.getAttribute("idAzienda")
                    )
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}