package it.unical.fleetgo.backend.Controller;

import com.stripe.exception.StripeException;
import it.unical.fleetgo.backend.Exceptions.*;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> gestisciErroreDatabase(SQLException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Si è verificato un errore durante le operazioni nel Database");
    }

    @ExceptionHandler(ManutenzioneEsistente.class)
    public ResponseEntity<String> gestisciErroreGenerico(ManutenzioneEsistente e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(VeicoloAssegnato.class)
    public ResponseEntity<String> gestisciErroreVeicoloAssegnatoNonRimovibile(VeicoloAssegnato e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(NoleggiInCorsoDissociamentoVeicolo.class)
    public ResponseEntity<String> gestisciErroreDissociamentoVeicoloPerNoleggiInCorso(NoleggiInCorsoDissociamentoVeicolo e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(EmailEsistente.class)
    public ResponseEntity<String> gestisciEmailEsistente(EmailEsistente e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(TargaPresente.class)
    public ResponseEntity<String> gestisciTargaEsistente(TargaPresente e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PIVAEsistente.class)
    public ResponseEntity<String> gestisciPivaEsistente(PIVAEsistente e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(EmailNonEsistente.class)
    public ResponseEntity<String> gestisciEmailNonEsistente(EmailNonEsistente e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(CodiceOTPErrato.class)
    public ResponseEntity<String> gestisciOTPErrato(CodiceOTPErrato e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> gestisciIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<String> gestisciErroreStripe() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante creazione Stripe");
    }

    @ExceptionHandler(ModelloAssegnatoAVeicoli.class)
    public ResponseEntity<String> gestisciErroreModelloNonEliminabile(ModelloAssegnatoAVeicoli e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(DissociazioneAziendaNonConsentita.class)
    public ResponseEntity<String> gestisciErroreDipendenteNonDissociabile(DissociazioneAziendaNonConsentita e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PrenotazioneEsistente.class)
    public ResponseEntity<String> gestisciErrorePrenotazioneEsistente(PrenotazioneEsistente e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(RichiestaManutenzioneNonValida.class)
    public ResponseEntity<String> gestisciRichiestaManutenzioneNonValida(RichiestaManutenzioneNonValida e) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NoleggiInCorsoPresenti.class)
    public ResponseEntity<String> gestisciErroreNoleggiInCorso(NoleggiInCorsoPresenti e) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(FattureDaGenerare.class)
    public ResponseEntity<String> gestisciErroreFattureDaGenerarePresenti(FattureDaGenerare e) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PrenotazioneNonEsistente.class)
    public ResponseEntity<String> gestisciErrorePrenotazioneNonEsistente(PrenotazioneNonEsistente e) {
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DatePrenotazioneNonValide.class)
    public ResponseEntity<String> gestisciErroreDateNonValide(DatePrenotazioneNonValide e) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(LuogoNonEliminabile.class)
    public ResponseEntity<String> gestisciLuogoNonEliminabile(LuogoNonEliminabile e) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> gestisciErroreInvioEmail(MessagingException e) {
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> gestisciErroreGenerico(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Si è verificato un errore imprevisto.");
    }
}