package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.OffertaDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerDashboard {

    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping("/getOfferte")
    public ResponseEntity<List<OffertaDTO>> getOfferteAttive() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getOfferteAttive()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getContatoreRichiesteAffiliazione")
    public ResponseEntity<Integer> getNumeroRichiesteAffiliazione(HttpSession session){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getNumRichiesteAffiliazione(
                            (Integer) session.getAttribute("idAzienda")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getContatoreRichiesteNoleggio")
    public ResponseEntity<Integer> getNumeroRichiesteNoleggio(HttpSession session){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getNumRichiesteNoleggio(
                            (Integer) session.getAttribute("idAzienda")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}