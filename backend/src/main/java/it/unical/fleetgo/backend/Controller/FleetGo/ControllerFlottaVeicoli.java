package it.unical.fleetgo.backend.Controller.FleetGo;
import it.unical.fleetgo.backend.Models.DTO.ModelloDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
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
public class ControllerFlottaVeicoli {

    @Autowired private VeicoloService veicoloService;
    @Autowired private SalvataggioImmagineService salvataggioImmagineVeicolo;

    @PostMapping(value = "/registraVeicolo")
    public ResponseEntity<String> registraVeicolo(@RequestBody VeicoloDTO veicolo) throws SQLException {
        veicoloService.registraVeicolo(veicolo);
        return ResponseEntity.status(HttpStatus.CREATED).body("Veicolo registrato con successo");
    }

    @PostMapping(value = "/eliminaVeicolo")
    public ResponseEntity<String> eliminaVeicolo(@RequestBody String targaVeicolo) throws SQLException {
        veicoloService.eliminaVeicolo(targaVeicolo);
        return ResponseEntity.status(HttpStatus.OK).body("Veicolo eliminato con successo");
    }

    @GetMapping(value = "/listaVeicoli")
    public ResponseEntity<List<VeicoloDTO>> getListaVeicoli() throws SQLException {
        List<VeicoloDTO> listaVeicoli = veicoloService.getListaVeicoli();
        return ResponseEntity.ok(listaVeicoli);
    }

    @GetMapping(value = "/informazioneVeicolo/{targa}")
    public ResponseEntity<VeicoloDTO> getInformazioneVeicolo(@PathVariable String targa) throws SQLException {
        VeicoloDTO veicolo = veicoloService.getInformazioniVeicolo(targa);
        return ResponseEntity.ok(veicolo);
    }

    @PostMapping(value = "/associaVeicoloAzienda")
    public ResponseEntity<String> associaVeicolo(@RequestBody VeicoloDTO veicolo) throws SQLException {
        veicoloService.associaVeicolo(veicolo);
        return ResponseEntity.status(HttpStatus.OK).body("Veicolo assegnato con successo");
    }

    @PostMapping(value = "/disassociaVeicoloAzienda")
    public ResponseEntity<String> disassociaVeicolo(@RequestBody VeicoloDTO veicolo) throws SQLException {
        veicoloService.disassociaVeicolo(veicolo);
        return ResponseEntity.status(HttpStatus.OK).body("Veicolo dissociato con successo");
    }

    @PostMapping(value = "/registraModello", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> registraModello(@RequestPart("modello") ModelloDTO modello, @RequestPart("immagine") MultipartFile immagine) throws IOException, SQLException {
        veicoloService.aggiuntaModello(modello, immagine);
        return ResponseEntity.status(HttpStatus.OK).body("Modello registrato con successo");
    }

    @GetMapping("/getModelli")
    public ResponseEntity<List<ModelloDTO>> getModelloInPiattaforma() throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(
                veicoloService.getModelli()
        );
    }

    @PostMapping("/eliminaModello")
    public ResponseEntity<String> eliminaModello(@RequestBody Integer idModello) throws SQLException {
        veicoloService.eliminaModello(idModello);
        return ResponseEntity.status(HttpStatus.OK).body("Modello eliminato con successo");
    }
}