package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.VeicoloService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerFlottaAziendale {

    @Autowired private VeicoloService veicoloService;
    @Autowired private AdminAziendaleService adminAziendaleService;

    @GetMapping(value = "/flottaAziendale")
    public ResponseEntity<List<VeicoloDTO>> getFlottaAziendale(HttpSession session) {
        try{
            Integer idAzienda = (Integer) session.getAttribute("idAzienda");;
            return ResponseEntity.ok(veicoloService.getListaVeicoliAziendali(idAzienda));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/impostaLuogo")
    public ResponseEntity<String> impostaLuogo(@RequestBody VeicoloDTO veicoloDTO, HttpSession session) {

        Integer idAzienda = (Integer) session.getAttribute("idAzienda");

        if(idAzienda == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try{
            veicoloDTO.setIdAziendaAffiliata(idAzienda);
            if(veicoloService.impostaLuogoVeicolo(veicoloDTO)) {
                return ResponseEntity.ok("Luogo impostato correttamente");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/inserisciRichiestaManutenzione")
    public ResponseEntity<String> inserisciRichiestaManutenzione(@RequestBody RichiestaManutenzioneDTO richiesta, HttpSession session) {

        Integer idAdmin = (Integer) session.getAttribute("idUtente");

        if(idAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try{
            richiesta.setIdAdminAzienda(idAdmin);
            adminAziendaleService.inserisciRichiestaDiManutenzione(richiesta);
            return ResponseEntity.status(HttpStatus.OK).body("Richiesta di manutenzione inviata con successo");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getRichiestaManutenzione/{idVeicolo}")
    public ResponseEntity<RichiestaManutenzioneDTO> getRichiestaManutenzione(@PathVariable Integer idVeicolo, HttpSession session) {
        Integer idAdmin = (Integer) session.getAttribute("idUtente");

        if(idAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    veicoloService.getManutenzioneVeicolo(idVeicolo, idAdmin)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/eliminaRichiesta")
    public ResponseEntity<String> eliminaRichiestaManutenzione(@RequestBody RichiestaManutenzioneDTO richiesta) {
        try{
            if(veicoloService.eliminaRichiestaManutenzione(richiesta)){
                return ResponseEntity.status(HttpStatus.OK).body("Richiesta eliminata con successo");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante l'eliminazione");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}