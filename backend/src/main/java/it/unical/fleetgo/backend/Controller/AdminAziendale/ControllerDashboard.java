package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
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

//    @GetMapping("/getRichiesteNoleggio")
//    public ResponseEntity<Integer> getRichiesteNoleggioInAttesa(HttpSession session) {
//        try {
//
//        }
//    }
}