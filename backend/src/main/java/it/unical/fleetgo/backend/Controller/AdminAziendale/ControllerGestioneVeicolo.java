package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Service.VeicoloService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")

public class ControllerGestioneVeicolo {
    @Autowired
    private VeicoloService veicoloService;



    @GetMapping(value = "/informazioneVeicolo/{targa}")
    public ResponseEntity<VeicoloDTO> getInformazioneVeicolo(@PathVariable String targa, HttpSession session) {
        try{
            VeicoloDTO veicolo = veicoloService.getInformazioniVeicolo(targa);
            return ResponseEntity.ok(veicolo);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
