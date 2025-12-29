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
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerModificaDati {

    @Autowired private AdminAziendaleService adminAziendaleService;
    @Autowired private UtenteService utenteService;
    @Autowired private AziendaService aziendaService;

    @PostMapping("/modificaDatiAdmin")
    public ResponseEntity<String> modificaDatiUtente(@RequestBody ModificaDatiUtenteDTO dati,HttpSession session) {

        Integer idUtente= (Integer)session.getAttribute("idUtente");

        if(idUtente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        dati.setIdUtente(idUtente);

        try{
            adminAziendaleService.modificaDati(dati);
            return  ResponseEntity.status(HttpStatus.OK).body("Dati modificati con successo!");

        }catch (RuntimeException | SQLException e){
            String errore = e.getMessage();

            if(errore.equals("Email già presente")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errore);
            }

            if(errore.equals("P.Iva già registrata da un'altra azienda")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errore);
            }

            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel sistema");
        }
    }

    @GetMapping("/datiUtente")
    public ResponseEntity<ModificaDatiUtenteDTO> invioDatiUtente(HttpSession session){

        Integer idUtente = (Integer)session.getAttribute("idUtente");

        if(idUtente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try{
            ModificaDatiUtenteDTO dati=utenteService.getDatiUtente(idUtente);
            return  ResponseEntity.status(HttpStatus.OK).body(dati);
        }catch (SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/luoghiAzienda")
    public ResponseEntity<List<LuogoDTO>> getLuoghiCorrenti(HttpSession session) {

        Integer idAzienda =  (Integer)session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getLuoghiCorrenti(idAzienda));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/aggiungiLuogo")
    public ResponseEntity<String> aggiungiLuogo(@RequestBody LuogoDTO luogo, HttpSession session){

        Integer idAzienda =  (Integer)session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            luogo.setIdAzienda(idAzienda);
            adminAziendaleService.aggiungiLuogo(luogo);
            return ResponseEntity.status(HttpStatus.OK).body("Luogo aggiunto con successo");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al DB");
        }
    }

    @PostMapping("/eliminaLuogo")
    public ResponseEntity<String> eliminaLuogo(@RequestBody Integer idLuogo){
        try {
            if(adminAziendaleService.eliminaLuogo(idLuogo))
                return ResponseEntity.status(HttpStatus.OK).body("Luogo aggiunto con successo");
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al DB");
        }
    }

    @PostMapping("/impostaSede")
    public ResponseEntity<String> impostaSede(@RequestBody Integer idLuogo, HttpSession session){

        Integer idAzienda =  (Integer)session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            if(aziendaService.impostaSede(idLuogo, idAzienda))
                return ResponseEntity.status(HttpStatus.OK).body("Sede modificati con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel sistema");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel sistema");
    }
}