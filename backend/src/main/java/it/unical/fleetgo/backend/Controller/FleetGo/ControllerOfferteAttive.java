package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.OffertaDTO;
import it.unical.fleetgo.backend.Service.FleetGoService;
import it.unical.fleetgo.backend.Service.SalvataggioImmagineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
public class ControllerOfferteAttive {

    @Autowired FleetGoService fleetGoService;

    @PostMapping(path = "/inserisciOfferta", consumes = { "multipart/form-data" })
    public ResponseEntity<String> inserisciNuovaOfferta(@RequestPart("offerta") OffertaDTO offerta, @RequestPart("immagine")MultipartFile immagine) throws IOException, SQLException {

        fleetGoService.inserisciNuovaOfferta(offerta, immagine);
        return ResponseEntity.status(HttpStatus.OK).body("Inserimento offerta riuscito");
    }

    @PostMapping("/eliminaOfferta")
    public ResponseEntity<String> eliminaOfferta(@RequestBody Integer idOfferta) throws SQLException {
        fleetGoService.eliminaOfferta(idOfferta);
        return ResponseEntity.status(HttpStatus.OK).body("Eliminazione offerta riuscito");
    }

    @GetMapping("/getOfferte")
    public ResponseEntity<List<OffertaDTO>> getOfferteAttive() throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(
                fleetGoService.getOfferteAttive()
        );
    }
}