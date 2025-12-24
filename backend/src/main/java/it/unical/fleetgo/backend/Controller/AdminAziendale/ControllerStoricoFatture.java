package it.unical.fleetgo.backend.Controller.AdminAziendale;

import com.stripe.exception.StripeException;
import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
public class ControllerStoricoFatture {

    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping(value = "/getFattureEmesse")
    public ResponseEntity<List<FatturaDTO>> getFattureEmesse(HttpSession session){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getFattureEmesse(
                            (Integer) session.getAttribute("idAzienda")
                    )
            );
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/downloadFattura/{idFattura}")
    public ResponseEntity<byte[]> downloadFattura(@PathVariable Integer idFattura) {
        try {
            byte[] pdf = adminAziendaleService.downloadFattura(idFattura);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "fattura_" + idFattura + ".pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/pagaFattura/{idFattura}")
    public ResponseEntity<String> pagaFattura(@PathVariable Integer idFattura) {
        try {
            String urlPagamento = adminAziendaleService.pagaFattura(idFattura);
            return ResponseEntity.status(HttpStatus.OK).body(urlPagamento);
        } catch (StripeException e) {
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante creazione Stripe");
        } catch (SQLException e) {
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante reperimento fattura dal DB");
        }
    }

    @PostMapping("/fatturaPagata/{idFattura}")
    public ResponseEntity<String> contrassegnaFatturaPagata(@PathVariable Integer idFattura) {
        try {
            if(adminAziendaleService.contrassegnaFatturaPagata(idFattura))
                return ResponseEntity.status(HttpStatus.OK).body("fattura pagata regolarmente");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fattura non segnata come pagata");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fattura non segnata come pagata");
        }
    }

    @GetMapping("/getAnniDisponibili")
    public ResponseEntity<List<Integer>> getAnniDisponibili(HttpSession session){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getAnniFatture(
                            (Integer) session.getAttribute("idAzienda")
                    )
            );
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}