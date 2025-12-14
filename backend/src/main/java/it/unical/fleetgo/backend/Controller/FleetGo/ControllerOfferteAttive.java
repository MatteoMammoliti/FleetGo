package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.OffertaDTO;
import it.unical.fleetgo.backend.Service.FleetGoService;
import it.unical.fleetgo.backend.Service.SalvataggioImmagineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerOfferteAttive {

    @Autowired FleetGoService fleetGoService;
    @Autowired SalvataggioImmagineService salvataggioImmagineService;

    @PostMapping(path = "/inserisciOfferta", consumes = { "multipart/form-data" })
    public ResponseEntity<String> inserisciNuovaOfferta(@RequestPart("offerta") OffertaDTO offerta, @RequestPart("immagine")MultipartFile immagine) {
        try {
            String urlImmagine = this.salvataggioImmagineService.salvaImmagine(immagine, "immagini-patenti");
            offerta.setImmagineCopertina(urlImmagine);
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