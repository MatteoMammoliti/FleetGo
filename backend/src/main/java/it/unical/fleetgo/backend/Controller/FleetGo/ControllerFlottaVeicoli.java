package it.unical.fleetgo.backend.Controller.FleetGo;
import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import it.unical.fleetgo.backend.Service.SalvataggioImmagineService;
import it.unical.fleetgo.backend.Service.VeicoloService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/registraVeicolo")
    public ResponseEntity<String> registraVeicolo(@RequestPart("targaVeicolo") String targa,
                                                  @RequestPart("modello") String modello,
                                                  @RequestPart ("tipoDistribuzioneVeicolo") String distrubuzione,
                                                  @RequestPart("statusCondizioneVeicolo") String condizione,
                                                  @RequestPart("immagineVeicolo") MultipartFile immagineVeicolo,
                                                  HttpSession session) throws IOException {

        VeicoloDTO veicoloDTO = new VeicoloDTO();
        String urlImmagine = salvataggioImmagineVeicolo.salvaImmagine(immagineVeicolo, "immagini-veicolo");
        veicoloDTO.setTargaVeicolo(targa);
        veicoloDTO.setUrlImmagine(urlImmagine);
        veicoloDTO.setModello(modello);
        veicoloDTO.setTipoDistribuzioneVeicolo(distrubuzione);
        veicoloDTO.setStatusCondizioneVeicolo(condizione);
        veicoloDTO.setLivelloCarburante(100);

        try{
            veicoloService.registraVeicolo(veicoloDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Veicolo registrato con successo");
        }catch(SQLException e){
            String state = e.getSQLState();
            if(state.equals("23505")){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Targa già presente");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inserimento fallito a causa di un errore con il sistema");
        }
    }

    @PostMapping(value = "/eliminaVeicolo")
    public ResponseEntity<String> eliminaVeicolo(@RequestBody String targaVeicolo, HttpSession session) {
        try {
            veicoloService.eliminaVeicolo(targaVeicolo);
            return ResponseEntity.status(HttpStatus.OK).body("Veicolo eliminato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione del veicolo");
        }
    }

    @GetMapping(value = "/listaVeicoli")
    public ResponseEntity<List<VeicoloDTO>> getListaVeicoli(HttpSession session) {
        try {
            List<Veicolo> listaVeicoli = veicoloService.getListaVeicoli();
            List<VeicoloDTO> listaVeicoliDTO = new ArrayList<>();

            for(Veicolo v : listaVeicoli) {
                VeicoloDTO veicoloDTO = getVeicoloDTO(v);
                listaVeicoliDTO.add(veicoloDTO);
            }
            return ResponseEntity.ok(listaVeicoliDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/informazioneVeicolo/{targa}")
    public ResponseEntity<VeicoloDTO> getInformazioneVeicolo(@PathVariable String targa, HttpSession session) {
        try{
            Veicolo veicolo = veicoloService.getInformazioniVeicolo(targa);
            VeicoloDTO veicoloDTO = getVeicoloDTO(veicolo);
            LuogoDTO luogo = new LuogoDTO();
            luogo.setNomeLuogo(veicolo.getLuogo().getNomeLuogo());
            luogo.setLatitudine(veicolo.getLuogo().getLatitudine());
            luogo.setLongitudine(veicolo.getLuogo().getLongitudine());
            veicoloDTO.setLuogoRitiroDeposito(luogo);

            return ResponseEntity.ok(veicoloDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/modificaVeicolo")
    public ResponseEntity<String> modificaVeicolo(@RequestBody VeicoloDTO veicoloDTO, HttpSession session) {
        try{
            veicoloService.modificaDati(veicoloDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Veicolo modificato con successo");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la modifica del veicolo");
        }
    }

    private VeicoloDTO getVeicoloDTO(Veicolo v) {
        VeicoloDTO veicoloDTO = new VeicoloDTO();
        veicoloDTO.setIdVeicolo(v.getIdVeicolo());
        veicoloDTO.setTargaVeicolo(v.getTargaVeicolo());
        veicoloDTO.setUrlImmagine(v.getUrlImmagine());
        veicoloDTO.setModello(v.getModello());
        veicoloDTO.setTipoDistribuzioneVeicolo(v.getTipoDistribuzioneVeicolo());
        veicoloDTO.setLivelloCarburante(v.getLivelloCarburante());
        veicoloDTO.setStatusCondizioneVeicolo(v.getStatusCondizioneVeicolo());
        if(v.getNomeAziendaAffiliata()!=null){
            veicoloDTO.setNomeAziendaAffiliata(v.getNomeAziendaAffiliata());
            veicoloDTO.setIdAziendaAffiliata(v.getIdAziendaAffiliata());
        }else {
            veicoloDTO.setNomeAziendaAffiliata(null);
            veicoloDTO.setIdAziendaAffiliata(null);
        }
        return veicoloDTO;
    }
}