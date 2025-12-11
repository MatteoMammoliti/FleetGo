package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerGestioneDipendenti {

    @Autowired private AdminAziendaleService adminAziendaleService;
    @Autowired private UtenteService utenteService;

    @GetMapping("/getDipendenti")
    public ResponseEntity<List<DipendenteDTO>> getDipendenti(HttpSession session) {
        try {
            int idAzienda = (int) session.getAttribute("idAzienda");
            List<DipendenteDTO> dipendenti = adminAziendaleService.getDipendenti(idAzienda);
            return ResponseEntity.ok(dipendenti);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/rimuoviDipendente")
    public ResponseEntity<String> eliminaUtente(HttpSession sessione, @RequestBody Integer idUtente) {
        try {
            int idAzienda = (int) sessione.getAttribute("idAzienda");
            adminAziendaleService.rimuoviDipendente(idUtente, idAzienda);
            return ResponseEntity.ok("Utente eliminato con successo");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'eliminazione dell'utente");
        }
    }

    @GetMapping("/getDipendente")
    public ResponseEntity<DipendenteDTO> getDipendente(@RequestBody Integer idUtente) {
        try {
            DipendenteDTO d = utenteService.getDipendente(idUtente);
            return ResponseEntity.ok(d);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}