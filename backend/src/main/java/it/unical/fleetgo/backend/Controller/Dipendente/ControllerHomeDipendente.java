package it.unical.fleetgo.backend.Controller.Dipendente;

import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.StatisticheDipendenteDTO;
import it.unical.fleetgo.backend.Service.DipendenteService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardDipendente")
public class ControllerHomeDipendente {

    @Autowired private DipendenteService dipendenteService;

    @GetMapping("/prossimoViaggio")
    public ResponseEntity<RichiestaNoleggioDTO> getProssimoNoleggioDipendente(HttpSession session) throws SQLException {

        Integer idAzienda = (Integer) session.getAttribute("idAziendaAssociata");
        Integer idDipendente = (Integer) session.getAttribute("idUtente");

        if(idAzienda==null || idDipendente==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(this.dipendenteService.getProssimoNoleggioDipendente(idDipendente,idAzienda));
    }

    @GetMapping("/statisticheDipendente")
    public ResponseEntity<StatisticheDipendenteDTO> getStatisticheDipendente(HttpSession session) throws SQLException {
        Integer idAzienda=(Integer) session.getAttribute("idAziendaAssociata");
        Integer idDipendente = (Integer) session.getAttribute("idUtente");

        if(idAzienda==null || idDipendente==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(this.dipendenteService.getStatisticheDipendente(idDipendente,idAzienda));
    }

    @GetMapping("/luoghiAzienda")
    public ResponseEntity<List<LuogoDTO>> getLuoghiAzienda(HttpSession session) throws SQLException {

        Integer idAziendaAssociata=(Integer) session.getAttribute("idAziendaAssociata");
        if(idAziendaAssociata==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(this.dipendenteService.getLuoghiAziendaAssociata(idAziendaAssociata));
    }

    @GetMapping("/richiediNome")
    public  ResponseEntity<String> getRichiediNome(HttpSession session) throws SQLException {
        Integer idDipendente=(Integer) session.getAttribute("idUtente");

        if(idDipendente==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(this.dipendenteService.getNomeDipendente(idDipendente));
    }

    @PostMapping("/inviaSegnalazione")
    public ResponseEntity<String> inviaSegnalazione(@RequestBody String messaggio, HttpSession session) throws SQLException, MessagingException {
        Integer idDipendente = (Integer) session.getAttribute("idUtente");
        Integer idAzienda = (Integer) session.getAttribute("idAziendaAssociata");

        if(idDipendente==null ||  idAzienda==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        dipendenteService.inviaSegnalazione(messaggio, idDipendente, idAzienda);
        return ResponseEntity.status(HttpStatus.OK).body("Richiesta di assistenza invia con successo");
    }
}