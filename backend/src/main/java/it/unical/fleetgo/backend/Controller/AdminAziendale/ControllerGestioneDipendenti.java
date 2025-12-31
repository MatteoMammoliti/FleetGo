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

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
public class ControllerGestioneDipendenti {

    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping("/getDipendenti")
    public ResponseEntity<List<DipendenteDTO>> getDipendenti(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<DipendenteDTO> dipendenti = adminAziendaleService.getDipendenti(idAzienda);
        return ResponseEntity.ok(dipendenti);
    }

    @PostMapping("/rimuoviDipendente")
    public ResponseEntity<String> eliminaUtente(HttpSession session, @RequestBody Integer idUtente) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!adminAziendaleService.rimuoviDipendente(idUtente, idAzienda)){
            return ResponseEntity.badRequest().body("Risultano prenotazioni in corso o già approvate per il dipendente selezionato");
        }
        return ResponseEntity.ok("Utente eliminato con successo");
    }

    @GetMapping("/getRichiesteNoleggio/{idDipendente}")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getRichiesteNoleggio(@PathVariable Integer idDipendente, HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getRichiesteNoleggio(
                        idDipendente,
                        idAzienda
                )
        );
    }

    @GetMapping("/getRichiesteAffiliazione")
    public ResponseEntity<List<RichiestaAffiliazioneAziendaDTO>> getRichiesteAffiliazione(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getRichiesteAffiliazioneDaAccettare(
                        idAzienda
                )
        );
    }

    @PostMapping("/rispondiAffiliazione/{idDipendente}")
    public ResponseEntity<String> rispondiAffiliazione(@PathVariable Integer idDipendente, @RequestBody boolean risposta, HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        adminAziendaleService.rispondiRichiestaAffiliazione(idDipendente, idAzienda, risposta);
        return ResponseEntity.ok("Richiesta approvata con successo");
    }
}