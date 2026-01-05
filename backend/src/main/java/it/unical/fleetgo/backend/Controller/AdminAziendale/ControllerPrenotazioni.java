package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.RisoluzioneConflittiNoleggio;
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
public class ControllerPrenotazioni {

    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping(value = "/getPrenotazioni")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getPrenotazioni(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getPrenotazioni(
                        idAzienda
                )
        );
    }

    @GetMapping("/getPrenotazioni/{idRichiesta}")
    public ResponseEntity<RichiestaNoleggioDTO> getPrenotazioni(@PathVariable Integer idRichiesta) throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getRichiestaNoleggio(idRichiesta)
        );
    }

    @GetMapping("/getPrenotazioniDaAccettare")
    public ResponseEntity<List<RichiestaNoleggioDTO>> getPrenotazioniDaAccettare(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getRichiesteDaAccettare(
                        idAzienda
                )
        );
    }

    @GetMapping("/getNumeroNoleggiDaApprovare")
    public ResponseEntity<Integer> getNumeroRichiesteNoleggio(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getNumRichiesteNoleggio(
                        idAzienda
                )
        );
    }

    @PostMapping("/approvaRichiesta")
    public ResponseEntity<String> approvaRichiesta(@RequestBody Integer idRichiesta) throws SQLException {

        if(adminAziendaleService.approvaRichiestaNoleggio(idRichiesta))
            return  ResponseEntity.status(HttpStatus.OK).body("Richiesta approvata con successo");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }

    @PostMapping("/rifiutaRichiesta")
    public ResponseEntity<String> rifiutaRichiesta(@RequestBody Integer idRichiesta) throws SQLException {

        if(adminAziendaleService.rifiutaRichiesta(idRichiesta))
            return  ResponseEntity.status(HttpStatus.OK).body("Richiesta rifiutata con successo");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping("/accettazioneConRifiuto")
    public ResponseEntity<String> accettazioneConRifiuto(@RequestBody RisoluzioneConflittiNoleggio dto) throws SQLException {
        adminAziendaleService.approvazioneConRifiutoAutomatico(dto.getIdRichiestaDaApprovare(), dto.getIdRichiesteDaRifiutare());
        return ResponseEntity.status(HttpStatus.OK).body("Approvazione riuscita");
    }

}