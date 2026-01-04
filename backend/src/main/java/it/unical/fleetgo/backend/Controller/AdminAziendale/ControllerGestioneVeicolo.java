package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Service.VeicoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/dashboardAdminAziendale")
public class ControllerGestioneVeicolo {

    @Autowired private VeicoloService veicoloService;

    @GetMapping(value = "/informazioneVeicolo/{targa}")
    public ResponseEntity<VeicoloDTO> getInformazioneVeicolo(@PathVariable String targa) throws SQLException {
        VeicoloDTO veicolo = veicoloService.getInformazioniVeicolo(targa);
        return ResponseEntity.ok(veicolo);
    }
}