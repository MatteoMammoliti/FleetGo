package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.OffertaDTO;
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
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerDashboard {

    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping("/getOfferte")
    public ResponseEntity<List<OffertaDTO>> getOfferteAttive() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getOfferteAttive());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getContatoreRichiesteAffiliazione")
    public ResponseEntity<Integer> getNumeroRichiesteAffiliazione(HttpSession session){

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getNumRichiesteAffiliazione(idAzienda));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getContatoreRichiesteNoleggio")
    public ResponseEntity<Integer> getNumeroRichiesteNoleggio(HttpSession session){

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getNumRichiesteNoleggio(idAzienda));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getSpesaMensile")
    public ResponseEntity<Float> getSpesaMensile(HttpSession session) {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getSpesaMensile(idAzienda));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getNumeroNoleggi")
    public ResponseEntity<Integer> getNumeroNoleggi(HttpSession session) {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getVeicoliNoleggiati(idAzienda));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getNomeCognomeAdmin")
    public ResponseEntity<String> getNomeCognomeAdmin(HttpSession session) {

        Integer idUtente = (Integer) session.getAttribute("idUtente");

        if(idUtente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getNomeCognomeAdmin(idUtente));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getNomeAziendaGestita")
    public ResponseEntity<String> getNomeAziendaGestita(HttpSession session) {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getNomeAziendaGestita(idAzienda));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/richiediAppuntamento")
    public ResponseEntity<String> richiediAppuntamento(HttpSession session) {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            adminAziendaleService.richiediAppuntamento(idAzienda);
            return ResponseEntity.status(HttpStatus.OK).body("Richiesta inviata con successo");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'invio della mail");
        }
    }

    @GetMapping("/getNumFattureDaPagare")
    public ResponseEntity<Integer> getNumeroFattureDaPagare(HttpSession session) {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.getNumeroFattureDaPagare(idAzienda));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/isSedeImpostata")
    public ResponseEntity<Boolean> getIfSede(HttpSession session) {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    adminAziendaleService.isSedeImpostata(idAzienda));
        }  catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}