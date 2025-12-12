package it.unical.fleetgo.backend.Controller.FleetGo;
import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import it.unical.fleetgo.backend.Service.SalvataggioImmagineService;
import it.unical.fleetgo.backend.Service.VeicoloService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerFlottaVeicoli {

    @Autowired private VeicoloService veicoloService;
    @Autowired private SalvataggioImmagineService salvataggioImmagineVeicolo;

    @PostMapping(value = "/registraVeicolo", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> registraVeicolo(@RequestPart("veicolo") VeicoloDTO veicolo, @RequestPart("immagineVeicolo") MultipartFile immagineVeicolo) throws IOException {

        try{
            String urlImmagine = salvataggioImmagineVeicolo.salvaImmagine(immagineVeicolo, "immagini-veicolo");
            veicolo.setUrlImmagine(urlImmagine);
            veicolo.setLivelloCarburante(100);

            veicoloService.registraVeicolo(veicolo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Veicolo registrato con successo");
        }catch(SQLException e){
            String state = e.getSQLState();
            if(state.equals("23505")){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Targa già presente");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inserimento fallito a causa di un errore con il sistema");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il salvataggio dell'immagine");
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
    public ResponseEntity<List<VeicoloDTO>> getListaVeicoli(HttpSession session) {
        try {
            List<VeicoloDTO> listaVeicoli = veicoloService.getListaVeicoli();
            return ResponseEntity.ok(listaVeicoli);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/informazioneVeicolo/{targa}")
    public ResponseEntity<VeicoloDTO> getInformazioneVeicolo(@PathVariable String targa, HttpSession session) {
        try{
            VeicoloDTO veicolo = veicoloService.getInformazioniVeicolo(targa);
            return ResponseEntity.ok(veicolo);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/modificaVeicolo")
    public ResponseEntity<String> modificaVeicolo(@RequestBody VeicoloDTO veicoloDTO) {
        try{
            veicoloService.modificaDati(veicoloDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Veicolo modificato con successo");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la modifica del veicolo");
        }
    }
}