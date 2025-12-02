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

    @PostMapping("/modificaNomeAdmin")
    public ResponseEntity<String> modificaDatiUtente(@RequestPart("nome")String nome,@RequestPart("cognome")String cognome,@RequestPart("data") String data,
                                                     @RequestPart("email")String email,@RequestPart("nomeAzienda")String nomeAzienda,@RequestPart("sedeAzienda")String sedeAzienda,
                                                     @RequestPart("pIva")String piva,HttpSession session){
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        try{
            adminAziendale.modificaDati(nome,cognome,data,email,nomeAzienda,sedeAzienda,piva,idUtente);
            return  ResponseEntity.status(HttpStatus.CREATED).body("Dati modificati con successo!");
        }catch (SQLException e){
            String errore=e.getMessage();
            if(errore.equals("Email già presente")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errore);
            }
            if(errore.equals("P.Iva già registrata da un'altra azienda")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errore);
            }
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel sistema");
        }
    }


}
