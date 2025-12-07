package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreContatoriStatoVeicoli;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerDashboard {

    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping("/statoVeicoli")
    public ResponseEntity<ContenitoreContatoriStatoVeicoli> getStatoVeicolo(HttpSession session) {
        try {

            if(session.getAttribute("ruolo") != null && session.getAttribute("ruolo").equals("AdminAziendale")){

                Integer idAzienda = adminAziendaleService.getIdAziendaGestita(
                        (Integer) session.getAttribute("idUtente")
                );

                return ResponseEntity.ok(adminAziendaleService.getStatoVeicolo(idAzienda));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return null;
    }
}
