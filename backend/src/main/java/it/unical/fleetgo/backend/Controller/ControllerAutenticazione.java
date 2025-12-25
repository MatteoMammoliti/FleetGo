package it.unical.fleetgo.backend.Controller;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiModificaPasswordDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Service.SalvataggioImmagineService;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.annotations.processing.SQL;
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
    @Autowired private SalvataggioImmagineService salvataggioImmagineService;

    @PostMapping(value = "/registrazione", consumes = { "multipart/form-data" })
    public ResponseEntity<String> registrazione(@RequestPart("utente") DipendenteDTO utente, @RequestPart("immagine") MultipartFile immagine) throws IOException {
        String urlImg= salvataggioImmagineService.salvaImmagine(immagine, "immagini-patenti");

        utente.setUrlImmagine(urlImg);

        try{
            utenteService.registraUtente(utente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo");

        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email già utilizzata");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registrazione non avvenuta");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al Database");
        }
    }

    @PostMapping(value = "/richiediCodiceOTP", consumes = "multipart/form-data")
    public ResponseEntity<String> recuperoPassword(@RequestPart("email") String email) {
        try {
            utenteService.invioCodice(email);
            return ResponseEntity.status(HttpStatus.CREATED).body("Codice OTP inviato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Codice OTP già inviato");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'invio del codice OTP");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al Database");
        }
    }

    @PostMapping("/modificaPassword")
    public ResponseEntity<String> modificaPassword(@RequestBody ContenitoreDatiModificaPasswordDTO dati) {
        try {
            utenteService.modificaPassword(
                    dati.getEmail(),
                    Integer.parseInt(dati.getCodiceOTP()),
                    dati.getNuovaPassword());
            return ResponseEntity.status(HttpStatus.OK).body("Password modificata con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Codice OTP errato o scaduto");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la modifica della password");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al Database");
        }
    }
}