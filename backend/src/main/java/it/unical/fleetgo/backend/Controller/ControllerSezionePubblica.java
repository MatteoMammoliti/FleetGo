package it.unical.fleetgo.backend.Controller;

import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sezionePubblica")
public class ControllerSezionePubblica {

    @Autowired private EmailService emailService;

    @PostMapping("/formContatto")
    public ResponseEntity<String> formContatto(@RequestPart("messaggio") String messaggio,
                                               @RequestPart("oggetto") String oggetto,
                                               @RequestPart("NomeCognome") String nomeCognome,
                                               @RequestPart("emailMittente") String email) throws MessagingException {

        emailService.inviaMailRichiestaContattoSezionePubblica(nomeCognome, email, messaggio, oggetto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Contatto richiedato");
    }
}