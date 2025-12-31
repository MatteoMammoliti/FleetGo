package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiRegistrazioneAzienda;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.AziendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
public class ControllerAziendeAffiliate {

    @Autowired private AziendaService aziendaService;
    @Autowired private AdminAziendaleService adminAziendaleService;

    @PostMapping("/registraAziendaAdmin")
    public ResponseEntity<String> registraAziendaEAdmin(@RequestBody ContenitoreDatiRegistrazioneAzienda contenitoreDati) throws SQLException {
        adminAziendaleService.registraAdminEAzienda(contenitoreDati);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo");
    }

    @GetMapping("/elencoAziendeAttive")
    public ResponseEntity<List<AziendaDTO>> getElencoAziendeAttive() throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(
                aziendaService.getElencoAziendeAttive()
        );
    }

    @GetMapping("/elencoAziendeDisabilitate")
    public ResponseEntity<List<AziendaDTO>> getElencoAziendeDisabilitate() throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(
                aziendaService.getElencoAziendeDisabilitate()
        );
    }

    @PostMapping("/riabilitaAzienda")
    public ResponseEntity<String> riabilitaAzienda(@RequestBody Integer idAzienda) throws SQLException {
        aziendaService.riabilitaAzienda(idAzienda);
        return ResponseEntity.status(HttpStatus.OK).body("Azienda riabilitata con successo");
    }

    @PostMapping("/disabilitaAzienda")
    public ResponseEntity<String> eliminaAzienda(@RequestBody Integer idAzienda) throws SQLException {
        aziendaService.disabilitaAzienda(idAzienda);
        return ResponseEntity.status(HttpStatus.OK).body("Azienda eliminata con successo");
    }
}