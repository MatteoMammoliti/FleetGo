package it.unical.fleetgo.backend.Controller.FleetGo;
import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import it.unical.fleetgo.backend.Service.FleetGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerFattureEmesse {

    @Autowired private FleetGoService fleetGoService;

    @GetMapping("/downloadFattura/{idFattura}")
    public ResponseEntity<byte[]> downloadFattura(@PathVariable Integer idFattura) {

        try {
            byte[] pdf = fleetGoService.downloadFattura(idFattura);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "fattura_" + idFattura + ".pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getFatture/{anno}")
    public ResponseEntity<List<FatturaDTO>> getFattureEmesse(@PathVariable Integer anno) {
        try {
            return new ResponseEntity<>(fleetGoService.getFatturePerAnno(anno), HttpStatus.OK);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAnni")
    public ResponseEntity<List<Integer>> getAnni() {
        try {
            return new ResponseEntity<>(fleetGoService.getAnni(), HttpStatus.OK);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}