package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiRegistrazioneAzienda;
import it.unical.fleetgo.backend.Models.DTO.Utente.AdminAziendaleDTO;
import it.unical.fleetgo.backend.Service.AziendaService;
import it.unical.fleetgo.backend.Service.EmailService;
import it.unical.fleetgo.backend.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerAziendeAffiliate {

    @Autowired private AziendaService aziendaService;
    @Autowired private UtenteService utenteService;
    @Autowired private EmailService emailService;

    @PostMapping("/registraAziendaAdmin")
    public ResponseEntity<String> registraAziendaEAdmin(@RequestBody ContenitoreDatiRegistrazioneAzienda contenitoreDati) {
        System.out.println("registraAziendaEAdmin");

        AziendaDTO azienda = contenitoreDati.getAzienda();
        AdminAziendaleDTO adminAziendale = contenitoreDati.getAdminAziendale();

        try {
            int idAdminAziendale = utenteService.registraUtente(adminAziendale);
            azienda.setIdAdminAzienda(idAdminAziendale);
            aziendaService.registraAzienda(azienda);
            emailService.inviaCredenzialiAdAdminAziendale(
                    contenitoreDati.adminAziendale.getEmail(),
                    contenitoreDati.adminAziendale.getPassword()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email già utilizzata");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registrazione non avvenuta");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al Database");
        }
    }

    @GetMapping("/elencoAziendeAttive")
    public ResponseEntity<List<AziendaDTO>> getElencoAziendeAttive() {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(
                    aziendaService.getElencoAziendeAttive()
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/elencoAziendeDisabilitate")
    public ResponseEntity<List<AziendaDTO>> getElencoAziendeDisabilitate() {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(
                    aziendaService.getElencoAziendeDisabilitate()
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/riabilitaAzienda")
    public ResponseEntity<String> riabilitaAzienda(@RequestBody Integer idAzienda) {
        try {
            if(aziendaService.riabilitaAzienda(idAzienda)) {
                return ResponseEntity.status(HttpStatus.OK).body("Azienda riabilitata con successo");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Azienda non riabilitata");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al DB");
        }
    }

    @PostMapping("/disabilitaAzienda")
    public ResponseEntity<String> eliminaAzienda(@RequestBody Integer idAzienda) {
        try {
            aziendaService.disabilitaAzienda(idAzienda);
            return ResponseEntity.status(HttpStatus.OK).body("Azienda eliminata con successo");

        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}