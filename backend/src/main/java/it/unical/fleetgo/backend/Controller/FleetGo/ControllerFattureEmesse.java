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
public class ControllerFattureEmesse {

    @Autowired private FleetGoService fleetGoService;

    @GetMapping("/downloadFattura/{idFattura}")
    public ResponseEntity<byte[]> downloadFattura(@PathVariable Integer idFattura) throws SQLException {
        byte[] pdf = fleetGoService.downloadFattura(idFattura);

        if (pdf == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String nomeFileFattura = "Fattura_FleetGo_" + idFattura + ".pdf";
        headers.setContentDispositionFormData("attachment", nomeFileFattura);
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/getFatture/{anno}")
    public ResponseEntity<List<FatturaDTO>> getFattureEmesse(@PathVariable Integer anno) throws SQLException {
        return ResponseEntity.ok(fleetGoService.getFatturePerAnno(anno));
    }

    @GetMapping("/getAnni")
    public ResponseEntity<List<Integer>> getAnni() throws SQLException {
        return ResponseEntity.ok(fleetGoService.getAnni());
    }
}