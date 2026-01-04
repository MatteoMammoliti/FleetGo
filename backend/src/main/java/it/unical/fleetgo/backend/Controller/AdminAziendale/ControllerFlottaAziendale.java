package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.VeicoloService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
public class ControllerFlottaAziendale {

    @Autowired private VeicoloService veicoloService;
    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping(value = "/flottaAziendale")
    public ResponseEntity<List<VeicoloDTO>> getFlottaAziendale(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(veicoloService.getListaVeicoliAziendali(idAzienda));
    }

    @PostMapping(value = "/impostaLuogo")
    public ResponseEntity<String> impostaLuogo(@RequestBody VeicoloDTO veicoloDTO, HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        veicoloDTO.setIdAziendaAffiliata(idAzienda);
        veicoloService.impostaLuogoVeicolo(veicoloDTO);
        return ResponseEntity.ok("Luogo impostato correttamente");
    }

    @PostMapping("/inserisciRichiestaManutenzione")
    public ResponseEntity<String> inserisciRichiestaManutenzione(@RequestBody RichiestaManutenzioneDTO richiesta, HttpSession session) throws SQLException {

        Integer idAdmin = (Integer) session.getAttribute("idUtente");

        if(idAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        richiesta.setIdAdminAzienda(idAdmin);
        adminAziendaleService.inserisciRichiestaDiManutenzione(richiesta);
        return ResponseEntity.ok("Richiesta di manutenzione inviata con successo");
    }

    @GetMapping("/getRichiestaManutenzione/{idVeicolo}")
    public ResponseEntity<RichiestaManutenzioneDTO> getRichiestaManutenzione(@PathVariable Integer idVeicolo, HttpSession session) throws SQLException {
        Integer idAdmin = (Integer) session.getAttribute("idUtente");

        if(idAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(
                veicoloService.getManutenzioneVeicolo(idVeicolo, idAdmin)
        );
    }

    @PostMapping("/eliminaRichiesta")
    public ResponseEntity<String> eliminaRichiestaManutenzione(@RequestBody RichiestaManutenzioneDTO richiesta) throws SQLException {
        if(veicoloService.eliminaRichiestaManutenzione(richiesta)){
            return ResponseEntity.ok("Richiesta eliminata con successo");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante l'eliminazione");
    }
}