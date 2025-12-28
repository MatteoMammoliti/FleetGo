package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.RichiestaAffiliazioneAziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerGestioneDipendenti {

    @Autowired private AdminAziendaleService adminAziendaleService;

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
            if(!adminAziendaleService.rimuoviDipendente(idUtente, idAzienda)){
                return ResponseEntity.badRequest().body("Risultano prenotazioni in corso o già approvate per il dipendente selezionato");
            }
            return ResponseEntity.ok("Utente eliminato con successo");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'eliminazione dell'utente");
        }
    }

    @GetMapping("/getRichiesteNoleggio/{idDipendente}")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getRichiesteNoleggio(@PathVariable Integer idDipendente, HttpSession session) {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getRichiesteNoleggio(
                            idDipendente,
                            idAzienda
                    )
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getRichiesteAffiliazione")
    public ResponseEntity<List<RichiestaAffiliazioneAziendaDTO>> getRichiesteAffiliazione(HttpSession session) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getRichiesteAffiliazioneDaAccettare(
                            (Integer) session.getAttribute("idAzienda")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/rispondiAffiliazione/{idDipendente}")
    public ResponseEntity<String> rispondiAffiliazione(@PathVariable Integer idDipendente, @RequestBody boolean risposta, HttpSession session) {
        try {
            adminAziendaleService.rispondiRichiestaAffiliazione(idDipendente, (Integer) session.getAttribute("idAzienda"), risposta);
            return ResponseEntity.ok("Richiesta approvata con successo");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Richiesta non approvata");
        }
    }
}