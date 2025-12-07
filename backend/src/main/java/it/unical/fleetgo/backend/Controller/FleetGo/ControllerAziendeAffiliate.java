package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiRegistrazioneAzienda;
import it.unical.fleetgo.backend.Models.DTO.Utente.AdminAziendaleDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import it.unical.fleetgo.backend.Service.AziendaService;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerAziendeAffiliate {

    @Autowired private AziendaService aziendaService;
    @Autowired private UtenteService utenteService;

    @PostMapping("/registraAzienda&Admin")
    public ResponseEntity<String> registraAziendaEAdmin(@RequestBody ContenitoreDatiRegistrazioneAzienda contenitoreDati) {

        AziendaDTO azienda = contenitoreDati.getAzienda();
        AdminAziendaleDTO adminAziendale = contenitoreDati.getAdminAziendale();

        try {
            int idAdminAziendale = utenteService.registraUtente(adminAziendale);
            azienda.setIdAdminAzienda(idAdminAziendale);
            aziendaService.registraAzienda(azienda);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email già utilizzata");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registrazione non avvenuta");
        }
    }

    @GetMapping("/elencoAziende")
    public ResponseEntity<List<AziendaDTO>> getElencoAziende(HttpSession session) {
        try {
            if(session.getAttribute("ruolo") != null && session.getAttribute("ruolo").equals("FleetGo")) {
                List<Azienda> elencoAziende = aziendaService.getElencoAziende();
                List<AziendaDTO> listaAziende = new ArrayList<>();

                for(Azienda a : elencoAziende) {
                    AziendaDTO aziendaDTO = new AziendaDTO();
                    aziendaDTO.setIdAzienda(a.getIdAzienda());
                    aziendaDTO.setNomeAzienda(a.getNomeAzienda());
                    aziendaDTO.setSedeAzienda(a.getSedeAzienda());
                    aziendaDTO.setPIva(a.getPIva());
                    aziendaDTO.setIdAdminAzienda(a.getIdAdmin());
                    listaAziende.add(aziendaDTO);
                }
                return ResponseEntity.ok(listaAziende);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return null;
    }

    @PostMapping("/eliminaAzienda")
    public ResponseEntity<String> eliminaAzienda(@RequestBody Integer idAdminGestore, HttpSession session) {
        try {
            if(session.getAttribute("ruolo") != null && session.getAttribute("ruolo").equals("FleetGo")) {
                aziendaService.eliminaAzienda(idAdminGestore);
                utenteService.eliminaUtente(idAdminGestore);
                return ResponseEntity.status(HttpStatus.OK).body("Azienda eliminata con successo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'azienda");
        }
        return null;
    }
}