package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerDashboardFleetGo {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/statistiche")
    public ResponseEntity<ContenitoreStatisticheNumeriche> getStatisticheNumeriche(HttpSession session){
        try{
            if(session.getAttribute("ruolo")!=null && session.getAttribute("ruolo").equals("FleetGo")){
                return ResponseEntity.ok(utenteService.getStatisticheNumeriche());
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return null;
    }

}
