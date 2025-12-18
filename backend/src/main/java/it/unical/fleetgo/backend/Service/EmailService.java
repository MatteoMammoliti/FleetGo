package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Models.DTO.Utente.AdminAziendaleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired private JavaMailSender sender;

    public void inviaCodiceOtp(String emailDestinatario, Integer otpGenerato) {

        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom("matti.mm04@gmail.com");
        email.setTo(emailDestinatario);
        email.setSubject("FleetGO | Il tuo Codice di Recupero Password");

        email.setText("Ciao! \n\nEcco il tuo codice per reimpostare la password: "
                + otpGenerato + "\n\nIl codice scade tra 5 minuti.");

        sender.send(email);
    }

    public void inviaMailRichiestaAppuntamento(AdminAziendaleDTO mittente, String emailDestinatario) {

        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom(mittente.getEmail());
        email.setTo(emailDestinatario);
        email.setSubject("Appuntamento Richiesto da Admin Aziendale: " + mittente.getNomeUtente());

        email.setText("L'Admin Aziendale " + mittente.getNomeUtente() + " dell'azienda " + mittente.getAziendaGestita().getNomeAzienda() +
                " ha richiesto un appuntamento. \n\n Rispondi il prima possibile e fissa un orario e un giorno.");

        sender.send(email);
    }
}