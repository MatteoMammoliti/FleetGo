package it.unical.fleetgo.backend.Controller.Dipendente;

import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Service.DipendenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/dashboardDipendente")
public class ControllerImpostazioniDipendente {
    @Autowired private DipendenteService dipendenteService;

    @GetMapping("/dettagliUtente")
    public ResponseEntity<ModificaDatiUtenteDTO> dettagliUtente(HttpSession session) throws SQLException {
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        if(idUtente==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(this.dipendenteService.getDatiUtente(idUtente));
    }

    @GetMapping("/getUrlPatente")
    public ResponseEntity<String> getUrlPatente(HttpSession session) throws SQLException {
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        if(idUtente==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(this.dipendenteService.getImmaginePatente(idUtente));
    }

    @PostMapping(path = "/aggiornaPatente", consumes = { "multipart/form-data" })
    public ResponseEntity<String> aggiornaImmaginePatente(@RequestPart("dati") MultipartFile immagine, HttpSession session) throws SQLException, IOException {
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        if(idUtente==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (immagine == null || immagine.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File mancante o vuoto");
        }

        this.dipendenteService.aggiornaUrlPatente(idUtente, immagine);
        return ResponseEntity.ok("Patente aggiornata con successo");
    }

    @PostMapping("/applicaModifiche")
    public ResponseEntity<String> applicaModifiche(@RequestBody ModificaDatiUtenteDTO dto,HttpSession session) throws SQLException {
        Integer idDipendente = (Integer) session.getAttribute("idUtente");
        if (idDipendente == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        dto.setIdUtente(idDipendente);
        this.dipendenteService.modificaDatiDipendente(dto);
        return ResponseEntity.ok("Modifiche applicate con successo");
    }

    @PostMapping("/lasciaAzienda")
    public ResponseEntity<String> lasciaAzienda(HttpSession session) throws SQLException {
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        Integer idAzienda =  (Integer) session.getAttribute("idAziendaAssociata");

        if(idUtente==null || idAzienda==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        dipendenteService.lasciaAzienda(idUtente, idAzienda);
        return ResponseEntity.ok("Azienda lasciata con successo");
    }
}