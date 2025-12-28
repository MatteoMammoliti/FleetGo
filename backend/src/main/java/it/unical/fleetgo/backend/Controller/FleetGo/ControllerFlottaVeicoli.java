package it.unical.fleetgo.backend.Controller.FleetGo;
import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.ModelloDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Service.AziendaService;
import it.unical.fleetgo.backend.Service.SalvataggioImmagineService;
import it.unical.fleetgo.backend.Service.VeicoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerFlottaVeicoli {

    @Autowired private VeicoloService veicoloService;
    @Autowired private SalvataggioImmagineService salvataggioImmagineVeicolo;
    @Autowired private AziendaService aziendaService;

    @PostMapping(value = "/registraVeicolo")
    public ResponseEntity<String> registraVeicolo(@RequestBody VeicoloDTO veicolo) {

        try {
            veicolo.setLivelloCarburante(100);
            veicoloService.registraVeicolo(veicolo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Veicolo registrato con successo");

        } catch (SQLException e) {
            String state = e.getSQLState();
            if (state.equals("23505")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Targa già presente");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inserimento fallito a causa di un errore con il sistema");
        }
    }

    @PostMapping(value = "/eliminaVeicolo")
    public ResponseEntity<String> eliminaVeicolo(@RequestBody String targaVeicolo) {
        try {
            veicoloService.eliminaVeicolo(targaVeicolo);
            return ResponseEntity.status(HttpStatus.OK).body("Veicolo eliminato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione del veicolo");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al Database");
        }
    }

    @GetMapping(value = "/listaVeicoli")
    public ResponseEntity<List<VeicoloDTO>> getListaVeicoli() {
        try {
            List<VeicoloDTO> listaVeicoli = veicoloService.getListaVeicoli();
            return ResponseEntity.ok(listaVeicoli);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/informazioneVeicolo/{targa}")
    public ResponseEntity<VeicoloDTO> getInformazioneVeicolo(@PathVariable String targa) {
        try {
            VeicoloDTO veicolo = veicoloService.getInformazioniVeicolo(targa);
            return ResponseEntity.ok(veicolo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/associaVeicoloAzienda")
    public ResponseEntity<String> associaVeicolo(@RequestBody VeicoloDTO veicolo) {
        try {
            veicoloService.associaVeicolo(veicolo);
            return ResponseEntity.status(HttpStatus.OK).body("Veicolo modificato con successo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la modifica del veicolo");
        }
    }

    @PostMapping(value = "/disassociaVeicoloAzienda")
    public ResponseEntity<String> disassociaVeicolo(@RequestBody VeicoloDTO veicolo) {
        try {
            veicoloService.disassociaVeicolo(veicolo);
            return ResponseEntity.status(HttpStatus.OK).body("Veicolo modificato con successo");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante le operazioni nel DB");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @PostMapping(value = "/registraModello", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> registraModello(@RequestPart("modello") ModelloDTO modello, @RequestPart("immagine") MultipartFile immagine) {
        try {
            String url = salvataggioImmagineVeicolo.salvaImmagine(immagine, "immagine-modelli");
            modello.setUrlImmagine(url);
            if(veicoloService.aggiuntaModello(modello)) {
                return ResponseEntity.status(HttpStatus.OK).body("Modello registrato con successo");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la registrazione del modello");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al Database");
        } catch (IOException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il salvataggio dell'immagine");
        }
    }

    @GetMapping("/getModelli")
    public ResponseEntity<List<ModelloDTO>> getModelloInPiattaforma() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    veicoloService.getModelli()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/eliminaModello")
    public ResponseEntity<String> eliminaModello(@RequestBody Integer idModello) {
        try {
            if(veicoloService.eliminaModello(idModello)) {
                return ResponseEntity.status(HttpStatus.OK).body("Modello eliminato con successo");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Modello non eliminato con successo");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di connessione al DB");
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Modello con veicoli. Non è possibile eliminarlo");
        }
    }
}