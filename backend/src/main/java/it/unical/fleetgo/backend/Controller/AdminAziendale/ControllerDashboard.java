package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.OffertaDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
public class ControllerDashboard {

    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping("/getOfferte")
    public ResponseEntity<List<OffertaDTO>> getOfferteAttive() throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(adminAziendaleService.getOfferteAttive());
    }

    @GetMapping("/getContatoreRichiesteAffiliazione")
    public ResponseEntity<Integer> getNumeroRichiesteAffiliazione(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getNumRichiesteAffiliazione(idAzienda));
    }

    @GetMapping("/getContatoreRichiesteNoleggio")
    public ResponseEntity<Integer> getNumeroRichiesteNoleggio(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getNumRichiesteNoleggio(idAzienda));
    }

    @GetMapping("/getSpesaMensile")
    public ResponseEntity<Float> getSpesaMensile(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getSpesaMensile(idAzienda));
    }

    @GetMapping("/getNumeroNoleggi")
    public ResponseEntity<Integer> getNumeroNoleggi(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getVeicoliNoleggiati(idAzienda));
    }

    @GetMapping("/getDatiGraficoTorta")
    public ResponseEntity<ContenitoreStatisticheNumeriche> getDatiGraficoTorta(HttpSession session) throws SQLException {
        Integer idAzienda = (Integer) session.getAttribute("idAzienda");
        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(adminAziendaleService.getDatiGraficoTorta(idAzienda));
    }

    @GetMapping("/getNomeCognomeAdmin")
    public ResponseEntity<String> getNomeCognomeAdmin(HttpSession session) throws SQLException {

        Integer idUtente = (Integer) session.getAttribute("idUtente");

        if(idUtente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getNomeCognomeAdmin(idUtente));
    }

    @GetMapping("/getNomeAziendaGestita")
    public ResponseEntity<String> getNomeAziendaGestita(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getNomeAziendaGestita(idAzienda));
    }

    @PostMapping("/richiediAppuntamento")
    public ResponseEntity<String> richiediAppuntamento(HttpSession session) throws SQLException, MessagingException {

        Integer idUtente = (Integer) session.getAttribute("idUtente");

        if(idUtente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        adminAziendaleService.richiediAppuntamento(idUtente);
        return ResponseEntity.status(HttpStatus.OK).body("Richiesta inviata con successo");
    }

    @GetMapping("/getNumFattureDaPagare")
    public ResponseEntity<Integer> getNumeroFattureDaPagare(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.getNumeroFattureDaPagare(idAzienda));
    }

    @GetMapping("/isSedeImpostata")
    public ResponseEntity<Boolean> getIfSede(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                adminAziendaleService.isSedeImpostata(idAzienda));
    }
}