package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerModificaDati {

    @Autowired private AdminAziendaleService adminAziendale;
    @Autowired private UtenteService utenteService;

//    public ControllerModificaDati(AdminAziendaleService adminAziendale, UtenteService utenteService) {
//        this.adminAziendale = adminAziendale;
//        this.utenteService = utenteService;
//    }

    @PostMapping("/modificaDatiAdmin")
    public ResponseEntity<String> modificaDatiUtente(@RequestBody ModificaDatiUtenteDTO dati,HttpSession session) {
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        dati.setIdUtente(idUtente);
        try{
            adminAziendale.modificaDati(dati);
            return  ResponseEntity.status(HttpStatus.OK).body("Dati modificati con successo!");
        }catch (RuntimeException | SQLException e){
            String errore=e.getMessage();
            if(errore.equals("Email già presente")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errore);
            }
            if(errore.equals("P.Iva già registrata da un'altra azienda")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errore);
            }
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel sistema");
        }
    }

    @GetMapping("/datiUtente")
    public ResponseEntity<ModificaDatiUtenteDTO> invioDatiUtente(HttpSession session){
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        ModificaDatiUtenteDTO dati;
        try{
            dati=utenteService.getDatiUtente(idUtente);
            return  ResponseEntity.status(HttpStatus.OK).body(dati);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}