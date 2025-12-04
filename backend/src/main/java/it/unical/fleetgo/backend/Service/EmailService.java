package it.unical.fleetgo.backend.Service;

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
}