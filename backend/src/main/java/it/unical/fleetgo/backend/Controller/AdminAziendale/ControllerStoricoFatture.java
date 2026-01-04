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
    public ResponseEntity<List<FatturaDTO>> getFattureEmesse(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getFattureEmesse(
                        idAzienda
                )
        );
    }

    @GetMapping("/downloadFattura/{idFattura}")
    public ResponseEntity<byte[]> downloadFattura(@PathVariable Integer idFattura) throws SQLException {
        byte[] pdf = adminAziendaleService.downloadFattura(idFattura);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String nomeFileFattura = "Fattura_FleetGo_" + idFattura + ".pdf";
        headers.setContentDispositionFormData("attachment", nomeFileFattura);
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @PostMapping("/pagaFattura/{idFattura}")
    public ResponseEntity<String> pagaFattura(@PathVariable Integer idFattura) throws StripeException, SQLException {
        String urlPagamento = adminAziendaleService.pagaFattura(idFattura);
        return ResponseEntity.status(HttpStatus.OK).body(urlPagamento);
    }

    @PostMapping("/fatturaPagata/{idFattura}")
    public ResponseEntity<String> contrassegnaFatturaPagata(@PathVariable Integer idFattura) throws SQLException {
        if(adminAziendaleService.contrassegnaFatturaPagata(idFattura))
            return ResponseEntity.status(HttpStatus.OK).body("fattura pagata regolarmente");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fattura non segnata come pagata");
    }

    @GetMapping("/getAnniDisponibili")
    public ResponseEntity<List<Integer>> getAnniDisponibili(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getAnniFatture(
                        idAzienda
                )
        );
    }
}