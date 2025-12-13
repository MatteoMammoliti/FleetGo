package it.unical.fleetgo.backend.Controller.AdminAziendale;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Service.VeicoloService;
import jakarta.servlet.ServletOutputStream;
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

    @GetMapping(value = "/flottaAziendale")
    public ResponseEntity<List<VeicoloDTO>> getFlottaAziendale( HttpSession session) {
        try{
            Integer idAzienda = (Integer) session.getAttribute("idAzienda");;
            return ResponseEntity.ok(veicoloService.getListaVeicoliAziendali(idAzienda));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}