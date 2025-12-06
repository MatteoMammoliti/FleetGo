package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerGestioneDipendenti {

    @Autowired private AdminAziendaleService adminAziendaleService;
    @Autowired private UtenteService utenteService;

    @GetMapping("/getDipendenti")
    public ResponseEntity<List<DipendenteDTO>> getDipendenti(HttpSession session) {
        try {

            if(session.getAttribute("ruolo") != null && session.getAttribute("ruolo").equals("AdminAziendale")) {


                int idUtente = (int) session.getAttribute("idUtente");
                int idAzienda = adminAziendaleService.getIdAziendaGestita(idUtente);

                List<Dipendente> dipendenti = adminAziendaleService.getDipendenti(idAzienda);

                List<DipendenteDTO> listaDipendenti = new ArrayList<>();

                for(Dipendente d : dipendenti) {
                    DipendenteDTO dipendenteDTO = new DipendenteDTO();
                    dipendenteDTO.setIdUtente(d.getIdUtente());
                    dipendenteDTO.setNomeUtente(d.getNomeUtente());
                    dipendenteDTO.setCognomeUtente(d.getCognomeUtente());
                    dipendenteDTO.setDataNascitaUtente(d.getDataNascitaUtente().toString());
                    dipendenteDTO.setIdAziendaAffiliata(d.getIdAziendaAffiliata());
                    listaDipendenti.add(dipendenteDTO);
                }
                return ResponseEntity.ok(listaDipendenti);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
        return null;
    }

    @PostMapping("/rimuoviDipendente")
    public ResponseEntity<String> eliminaUtente(HttpSession sessione, @RequestBody Integer idUtente) {
        try {
            if(sessione.getAttribute("ruolo") != null && sessione.getAttribute("ruolo").equals("AdminAziendale")) {

                int idAdmin = (int) sessione.getAttribute("idUtente");
                int idAzienda = adminAziendaleService.getIdAziendaGestita(idAdmin);

                adminAziendaleService.rimuoviDipendente(idUtente, idAzienda);
                return ResponseEntity.ok("Utente eliminato con successo");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'eliminazione dell'utente");
        }
        return null;
    }

    @GetMapping("/getDipendente")
    public ResponseEntity<DipendenteDTO> getDipendente(HttpSession session, Integer idUtente) {
        try {
            if(session.getAttribute("ruolo") != null && session.getAttribute("ruolo").equals("AdminAziendale")) {
                Dipendente d = utenteService.getDipendente(idUtente);

                DipendenteDTO dipendenteDTO = new DipendenteDTO();
                dipendenteDTO.setIdUtente(d.getIdUtente());
                dipendenteDTO.setNomeUtente(d.getNomeUtente());
                dipendenteDTO.setCognomeUtente(d.getCognomeUtente());
                dipendenteDTO.setIdAziendaAffiliata(d.getIdAziendaAffiliata());
                dipendenteDTO.setDataNascitaUtente(d.getDataNascitaUtente().toString());
                return ResponseEntity.ok(dipendenteDTO);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
        return null;
    }

}
