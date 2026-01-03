package it.unical.fleetgo.backend.Controller;
import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiModificaPasswordDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/autenticazione")
public class ControllerAutenticazione {

    @Autowired private UtenteService utenteService;


    @PostMapping(value = "/registrazione", consumes = { "multipart/form-data" })
    public ResponseEntity<String> registrazione(@RequestPart("utente") DipendenteDTO utente,
                                                @RequestPart("immagine") MultipartFile immagine) throws IOException, SQLException {
        utenteService.registraUtente(utente, immagine);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo");
    }

    @PostMapping(value = "/richiediCodiceOTP", consumes = "multipart/form-data")
    public ResponseEntity<String> recuperoPassword(@RequestPart("email") String email) throws SQLException, MessagingException {
        utenteService.invioCodice(email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Codice OTP inviato con successo");
    }

    @PostMapping("/modificaPassword")
    public ResponseEntity<String> modificaPassword(@RequestBody ContenitoreDatiModificaPasswordDTO dati) throws SQLException {
        utenteService.modificaPassword(
                dati.getEmail(),
                Integer.parseInt(dati.getCodiceOTP()),
                dati.getNuovaPassword());
        return ResponseEntity.status(HttpStatus.OK).body("Password modificata con successo");
    }
}