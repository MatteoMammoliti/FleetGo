package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.Utente.AdminAziendaleDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final static String EMAIL_FLEET_GO = "fleetgo2025@gmail.com";
    @Autowired private JavaMailSender sender;
    @Autowired private TemplateEngine templateEngine;


    private void elaboraEdInviaEmail(String destinatario,
                                     String oggetto,
                                     String templateHtml,
                                     Map<String, Object> variabiliEmail,
                                     String emailACuiRispondore) throws MessagingException {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(variabiliEmail);

            String htmlBody  = templateEngine.process(templateHtml, context);

            helper.setTo(destinatario);
            helper.setFrom(EMAIL_FLEET_GO);
            helper.setSubject(oggetto);
            helper.setText(htmlBody, true);

            if(emailACuiRispondore != null && !emailACuiRispondore.isEmpty()) {
                helper.setReplyTo(emailACuiRispondore);
            }

            sender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new MessagingException();
        }
    }


    @Async
    public void inviaCodiceOtp(String emailDestinatario, Integer otpGenerato) throws MessagingException {

        Map<String, Object> variabiliEmail = new HashMap<>();

        String testo = "Di seguito il codice OTP da inserire per completare la procedura.";
        variabiliEmail.put("otp", otpGenerato);
        variabiliEmail.put("titolo", "Procedura di Modifica Password");
        variabiliEmail.put("messaggio", testo);
        String oggetto = "FleetGO | Il tuo Codice di Recupero Password";

        this.elaboraEdInviaEmail(
                emailDestinatario,
                oggetto,
                "email-otp",
                variabiliEmail,
                null
        );
    }

    @Async
    public void inviaMailRichiestaAppuntamento(AdminAziendaleDTO mittente, String emailAdmin) throws MessagingException {

        Map<String, Object> variabiliEmail = new HashMap<>();
        variabiliEmail.put("nomeAdmin", mittente.getNomeUtente());
        variabiliEmail.put("nomeAzienda", mittente.getAziendaGestita().getNomeAzienda());
        variabiliEmail.put("emailAdmin", emailAdmin);
        String oggetto = "Richiesta Appuntamento: " + mittente.getNomeUtente();

        this.elaboraEdInviaEmail(
               EMAIL_FLEET_GO,
                oggetto,
                "email-appuntamento",
                variabiliEmail,
                emailAdmin
        );
    }

    @Async
    public void inviaMailRichiestaContattoSezionePubblica(String nomeCognome, String email, String oggetto, String messaggio) throws MessagingException {

        Map<String, Object> variabiliEmail = new HashMap<>();
        variabiliEmail.put("nomeCompleto", nomeCognome);
        variabiliEmail.put("emailUtente", email);
        variabiliEmail.put("messaggio", messaggio);
        String oggettoCompleto = "Richiesta di contatto: " + oggetto;

        this.elaboraEdInviaEmail(
                EMAIL_FLEET_GO,
                oggettoCompleto,
                "email-assistenza-sezione-pubblica",
                variabiliEmail,
                email
        );
    }

    @Async
    public void inviaCredenzialiAdAdminAziendale(String emailDestinatario, String password) throws MessagingException {
        Map<String, Object> variabiliEmail = new HashMap<>();
        variabiliEmail.put("emailAdmin", emailDestinatario);
        variabiliEmail.put("password", password);
        variabiliEmail.put("loginLink", "http://localhost:4200/login");

        String oggetto = "Credenziali Accesso Admin Aziendale | FleetGo";

        this.elaboraEdInviaEmail(
                emailDestinatario,
                oggetto,
                "email-invio-credenziali",
                variabiliEmail,
                null
        );
    }

    @Async
    public void inviaRichiestaAssistenza(String emailMittente, String emailDestinatario, String messaggio, Integer idDipendente) throws MessagingException {

        Map<String, Object> variabiliEmail = new HashMap<>();
        variabiliEmail.put("idDipendente", idDipendente);
        variabiliEmail.put("emailMittente", emailMittente);
        variabiliEmail.put("messaggio", messaggio);

        String oggetto = "Richiesta di assistenza da Dipendente ID: " + idDipendente;

        this.elaboraEdInviaEmail(
                emailDestinatario,
                oggetto,
                "email-richiesta-assistenza-dipendente",
                variabiliEmail,
                emailMittente
        );
    }
}