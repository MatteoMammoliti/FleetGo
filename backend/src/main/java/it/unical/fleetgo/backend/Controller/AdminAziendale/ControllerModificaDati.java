package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.AziendaService;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
public class ControllerModificaDati {

    @Autowired private AdminAziendaleService adminAziendaleService;
    @Autowired private UtenteService utenteService;
    @Autowired private AziendaService aziendaService;

    @PostMapping("/modificaDatiAdmin")
    public ResponseEntity<String> modificaDatiUtente(@RequestBody ModificaDatiUtenteDTO dati,HttpSession session) throws SQLException {

        Integer idUtente= (Integer)session.getAttribute("idUtente");

        if(idUtente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        dati.setIdUtente(idUtente);
        adminAziendaleService.modificaDati(dati);
        return  ResponseEntity.status(HttpStatus.OK).body("Dati modificati con successo!");
    }

    @GetMapping("/datiUtente")
    public ResponseEntity<ModificaDatiUtenteDTO> invioDatiUtente(HttpSession session) throws SQLException {

        Integer idUtente = (Integer)session.getAttribute("idUtente");

        if(idUtente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ModificaDatiUtenteDTO dati=utenteService.getDatiUtente(idUtente);
        return  ResponseEntity.status(HttpStatus.OK).body(dati);
    }

    @GetMapping("/luoghiAzienda")
    public ResponseEntity<List<LuogoDTO>> getLuoghiCorrenti(HttpSession session) throws SQLException {

        Integer idAzienda =  (Integer)session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getLuoghiCorrenti(idAzienda));
    }

    @PostMapping("/aggiungiLuogo")
    public ResponseEntity<String> aggiungiLuogo(@RequestBody LuogoDTO luogo, HttpSession session) throws SQLException {

        Integer idAzienda =  (Integer)session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        luogo.setIdAzienda(idAzienda);
        adminAziendaleService.aggiungiLuogo(luogo);
        return ResponseEntity.status(HttpStatus.OK).body("Luogo aggiunto con successo");
    }

    @PostMapping("/eliminaLuogo")
    public ResponseEntity<String> eliminaLuogo(@RequestBody Integer idLuogo) throws SQLException {
        if(adminAziendaleService.eliminaLuogo(idLuogo))
            return ResponseEntity.status(HttpStatus.OK).body("Luogo eliminato con successo");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile eliminare il luogo");
    }

    @PostMapping("/impostaSede")
    public ResponseEntity<String> impostaSede(@RequestBody Integer idLuogo, HttpSession session) throws SQLException {

        Integer idAzienda =  (Integer)session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if(aziendaService.impostaSede(idLuogo, idAzienda))
            return ResponseEntity.status(HttpStatus.OK).body("Sede modificati con successo!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel sistema");
    }
}