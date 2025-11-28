package it.unical.fleetgo.backend.Controller;

import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Service.SalvataggioPatenteService;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/autenticazione")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerAutenticazione {

    @Autowired private UtenteService utenteService;
    @Autowired private SalvataggioPatenteService salvataggioPatenteService;

    @PostMapping(value = "/registrazione", consumes = { "multipart/form-data" })
    public ResponseEntity<String> registrazione(@RequestPart("utente") DipendenteDTO utente, @RequestPart("immagine") MultipartFile immagine) throws IOException {
        String urlImg= salvataggioPatenteService.salvaImmagine(immagine);

        utente.setUrlImmagine(urlImg);

        try {

            utenteService.registraUtente(utente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo");

        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email già utilizzata");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registrazione non avvenuta");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password,HttpSession session){
        try{
            Integer idUtente = utenteService.loginUtente(email,password);
            if(idUtente!=null){
                session.setAttribute("idUtente",idUtente);
                session.setAttribute("ruolo",utenteService.getRuolo(idUtente));
                return ResponseEntity.status(HttpStatus.CREATED).body("Login avvenuta con successo");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password e/o email non corrette");
        }catch (SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login non avvenuta a causa di un errore nel sistema");
        }
    }
}