package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Persistence.Entity.Utente.AdminAziendale;
import it.unical.fleetgo.backend.Service.ServiceAdminAziendale;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/dashboardAdminAziendale")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerAdminAziendale {

    private final ServiceAdminAziendale adminAziendale;

    public ControllerAdminAziendale(ServiceAdminAziendale adminAziendale) {
        this.adminAziendale = adminAziendale;
    }

    @PostMapping("/modificaDatiAdmin")
    public ResponseEntity<String> modificaDatiUtente(@RequestPart(value = "nome",required = false)String nome,
                                                     @RequestPart(value = "cognome",required = false)String cognome,
                                                     @RequestPart(value = "data",required = false) String data,
                                                     @RequestPart(value = "email",required = false)String email,
                                                     @RequestPart(value = "nomeAzienda",required = false)String nomeAzienda,
                                                     @RequestPart(value = "sedeAzienda",required = false)String sedeAzienda,
                                                     @RequestPart(value = "pIva",required = false)String piva,HttpSession session){
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        try{
            adminAziendale.modificaDati(nome,cognome,data,email,nomeAzienda,sedeAzienda,piva,idUtente);
            return  ResponseEntity.status(HttpStatus.OK).body("Dati modificati con successo!");
        }catch (RuntimeException | SQLException e){
            String errore=e.getMessage();
            if(errore.equals("Email già presente")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errore);
            }
            if(errore.equals("P.Iva già registrata da un'altra azienda")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errore);
            }
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel sistema");
        }
    }


}
