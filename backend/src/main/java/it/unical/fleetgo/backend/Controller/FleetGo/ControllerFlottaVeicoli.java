package it.unical.fleetgo.backend.Controller.FleetGo;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import it.unical.fleetgo.backend.Service.SalvataggioImmagineVeicolo;
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
    @Autowired private SalvataggioImmagineVeicolo salvataggioImmagineVeicolo;

    @PostMapping("/registraVeicolo")
    public ResponseEntity<String> registraVeicolo(@RequestPart("targaVeicolo") String targa,@RequestPart("modello") String modello,@RequestPart ("tipoDistribuzioneVeicolo") String distrubuzione,
                                                  @RequestPart("statusCondizioneVeicolo") String condizione,@RequestPart("immagineVeicolo") MultipartFile immagineVeicolo ) throws IOException {
        VeicoloDTO veicoloDTO = new VeicoloDTO();
        String urlImmagine = salvataggioImmagineVeicolo.salvaImmagine(immagineVeicolo);
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
    public ResponseEntity<String> eliminaVeicolo(@RequestPart("targaVeicolo") String targaVeicolo) {
        try {
            veicoloService.eliminaVeicolo(targaVeicolo);
            return ResponseEntity.status(HttpStatus.OK).body("Veicolo eliminato con successo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione del veicolo");
        }
    }

    @GetMapping(value = "/listaVeicoli")
    public ResponseEntity<List<VeicoloDTO>> getListaVeicoli(HttpSession session) {
        try {
            if(session.getAttribute("ruolo")!=null && session.getAttribute("ruolo").equals("FleetGo")){
                List<Veicolo> listaVeicoli = veicoloService.getListaVeicoli();
                List<VeicoloDTO> listaVeicoliDTO = new ArrayList<>();

                for(Veicolo v : listaVeicoli) {
                    VeicoloDTO veicoloDTO = getVeicoloDTO(v);
                    listaVeicoliDTO.add(veicoloDTO);
                }
                return ResponseEntity.ok(listaVeicoliDTO);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return null;
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
        }else {
            veicoloDTO.setNomeAziendaAffiliata(null);
        }
        return veicoloDTO;
    }
}