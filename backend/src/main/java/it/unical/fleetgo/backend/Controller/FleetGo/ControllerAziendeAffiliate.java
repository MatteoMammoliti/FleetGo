package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiRegistrazioneAzienda;
import it.unical.fleetgo.backend.Models.DTO.Utente.AdminAziendaleDTO;
import it.unical.fleetgo.backend.Service.AziendaService;
import it.unical.fleetgo.backend.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerAziendeAffiliate {

    @Autowired private AziendaService aziendaService;
    @Autowired private UtenteService utenteService;

    @PostMapping("/registraAzienda&Admin")
    public ResponseEntity<String> registraAziendaEAdmin(@RequestBody ContenitoreDatiRegistrazioneAzienda contenitoreDati) {

        AziendaDTO azienda = contenitoreDati.getAzienda();
        AdminAziendaleDTO adminAziendale = contenitoreDati.getAdminAziendale();

        try {
            int idAdminAziendale = utenteService.registraUtente(adminAziendale);
            azienda.setIdAdminAzienda(idAdminAziendale);
            aziendaService.registraAzienda(azienda);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email già utilizzata");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registrazione non avvenuta");
        }
    }
}