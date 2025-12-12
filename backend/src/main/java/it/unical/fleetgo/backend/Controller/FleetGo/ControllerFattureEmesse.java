package it.unical.fleetgo.backend.Controller.FleetGo;
import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import it.unical.fleetgo.backend.Service.GeneratorePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/dashboardFleetGo")
public class ControllerFattureEmesse {

    @Autowired private GeneratorePdfService generatorePdfService;

//    @GetMapping("/downloadFattura/{idFattura}")
//    public ResponseEntity<byte[]> downloadFattura(@PathVariable Integer idFattura) {
//
//        try {
//            FatturaDTO fattura = fatturaService.getFattura(idFattura);
//
//            byte[] pdf = generatorePdfService.generaPdfFattura(fattura);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("attachment", "fattura_" + idFattura + ".pdf");
//            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @GetMapping("/getFattureEmesse")
//    public ResponseEntity<List<FatturaDTO>> getFattureEmesse() {}
}